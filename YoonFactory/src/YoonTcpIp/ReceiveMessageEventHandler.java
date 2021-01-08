package YoonTcpIp;

import YoonCommon.eYoonStatus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ReceiveMessageEventHandler {

    private static final int MAX_THREAD_POOL = 5;

    private static List<IReceiveMessageEventListener> m_pListListener = new CopyOnWriteArrayList<IReceiveMessageEventListener>();

    private static synchronized List<IReceiveMessageEventListener> GetListenerList() {
        return m_pListListener;
    }

    public static synchronized void AddListener(IReceiveMessageEventListener eventListener) {
        if (GetListenerList().indexOf(eventListener) == -1) {
            m_pListListener.add(eventListener);
        }
    }

    public static synchronized void RemoveListener(IReceiveMessageEventListener eventListener) {
        if (GetListenerList().indexOf(eventListener) != -1) {
            m_pListListener.remove(eventListener);
        }
    }

    public static synchronized void CallEvent(final Class<?> caller, final String strMessage) {
        CallEvent(caller, strMessage, true);
    }

    public static synchronized void CallEvent(final Class<?> caller, final String strMessage, boolean bDoAsync) {
        if (bDoAsync) {
            CallEventByAsync(caller, strMessage);
        } else {
            CallEventBySync(caller, strMessage);
        }
    }

    private static synchronized void CallEventByAsync(final Class<?> caller, final String strMessage) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        for (final IReceiveMessageEventListener listener : m_pListListener) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!listener.getClass().getName().equals(caller.getName())) {
                        listener.onEvent(strMessage);
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private static synchronized void CallEventBySync(final Class<?> caller, final String strMessage) {
        for (final IReceiveMessageEventListener listener : m_pListListener) {
            if (!listener.getClass().getName().equals(caller.getName())) {
                listener.onEvent(strMessage);
            }
        }
    }
}