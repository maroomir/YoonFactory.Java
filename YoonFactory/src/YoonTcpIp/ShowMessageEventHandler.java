package YoonTcpIp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import YoonCommon.eYoonStatus;

public class ShowMessageEventHandler {

    private static final int MAX_THREAD_POOL = 5;

    private static List<IShowMessageEventListener> m_pListListener = new CopyOnWriteArrayList<IShowMessageEventListener>();

    private static synchronized List<IShowMessageEventListener> GetListenerList() {
        return m_pListListener;
    }

    public static synchronized void AddListener(IShowMessageEventListener eventListener) {
        if (GetListenerList().indexOf(eventListener) == -1) {
            m_pListListener.add(eventListener);
        }
    }

    public static synchronized void RemoveListener(IShowMessageEventListener eventListener) {
        if (GetListenerList().indexOf(eventListener) != -1) {
            m_pListListener.remove(eventListener);
        }
    }

    public static synchronized void CallEvent(final Class<?> caller, final eYoonStatus nStatus, final String strMessage) {
        CallEvent(caller, nStatus, strMessage, true);
    }

    public static synchronized void CallEvent(final Class<?> caller, final eYoonStatus nStatus, final String strMessage, boolean bDoAsync) {
        if (bDoAsync) {
            CallEventByAsync(caller, nStatus, strMessage);
        } else {
            CallEventBySync(caller, nStatus, strMessage);
        }
    }

    private static synchronized void CallEventByAsync(final Class<?> caller, final eYoonStatus nStatus, final String strMessage) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IShowMessageEventListener listener : m_pListListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.OnEvent(nStatus, strMessage);
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private static synchronized void CallEventBySync(final Class<?> caller, final eYoonStatus nStatus, final String strMessage) {
        for (final IShowMessageEventListener listener : m_pListListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.OnEvent(nStatus, strMessage);
            }
        }
    }
}