package YoonCommon;

public interface IYoonTemplate {
    void copyFrom(IYoonTemplate pTemplate);

    boolean saveTemplate();

    boolean loadTemplate();

    IYoonContainer getContainer();

    void setContainer(IYoonContainer pContainer);
}
