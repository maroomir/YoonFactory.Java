package YoonCommon;

public interface IYoonTemplate {
    void CopyFrom(IYoonTemplate pTemplate);

    IYoonTemplate Clone();

    boolean SaveTemplate();

    boolean LoadTemplate();

    IYoonContainer GetContainer();

    void SetContainer(IYoonContainer pContainer);
}
