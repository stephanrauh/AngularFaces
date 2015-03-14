package de.beyondjava.angularFaces.core.i18n;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagConfig;

public class LanguageAwareTagConfig implements ComponentConfig {

    protected final TagConfig parent;

    protected final String componentType;

    protected final String rendererType;

    public LanguageAwareTagConfig(TagConfig parent, String componentType, String rendererType)
    {
        this.parent = parent;
        this.componentType = componentType;
        this.rendererType = rendererType;
    }

    public String getComponentType()
    {
        return this.componentType;
    }

    public String getRendererType()
    {
        return this.rendererType;
    }

    public FaceletHandler getNextHandler()
    {
        return this.parent.getNextHandler();
    }

    public Tag getTag()
    {
        return this.parent.getTag();
    }

    public String getTagId()
    {
        return this.parent.getTagId();
    }
}