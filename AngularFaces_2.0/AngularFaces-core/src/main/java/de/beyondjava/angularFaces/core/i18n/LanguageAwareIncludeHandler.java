package de.beyondjava.angularFaces.core.i18n;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

import de.beyondjava.angularFaces.core.Configuration;
import de.beyondjava.angularFaces.core.tagTransformer.AFTagAttributes;
import de.beyondjava.angularFaces.core.tagTransformer.TagAttributeUtilities;

public class LanguageAwareIncludeHandler extends TagHandler implements FaceletHandler {

    private final TagAttribute src;

    private final ComponentConfig config;

    /**
     * @param config
     */
    public LanguageAwareIncludeHandler(ComponentConfig config) {
        super(config);
        TagAttribute attr = null;
        attr = config.getTag().getAttributes().get("src");
        if (null == attr) {
            attr = config.getTag().getAttributes().get("file");
        }
        if (null == attr) {
            attr = config.getTag().getAttributes().get("page");
        }
        if (null == attr) {
            throw new TagException(config.getTag(), "Attribute 'src', 'file' or 'page' is required");
        }
        this.src = attr;
        this.config = config;
    }

    public static void includeCompositeComponent(UIComponent parent, String taglibURI, String tagName, String id) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent composite = context.getApplication().getViewHandler()
                .getViewDeclarationLanguage(context, context.getViewRoot().getViewId())
                .createComponent(context, taglibURI, tagName, null);
        composite.setId(id);
        parent.getChildren().add(composite);
    }

    @Override
    public void apply(final FaceletContext ctx, UIComponent parent) throws IOException {
        Iterator<Locale> locales = FacesContext.getCurrentInstance().getExternalContext().getRequestLocales();

        while (locales.hasNext()) {
            String language = locales.next().getLanguage();
            try {
                tryLanguage(ctx, parent, language);
                return;
            } catch (ReflectiveOperationException ex) {
            }
            catch (FileNotFoundException ex) {
                
            }
        }
        try {
            tryLanguage(ctx, parent, null);
            return;
        } catch (ReflectiveOperationException ex) {
            throw new FacesException("Couldn't find the language file", ex);
        }

    }

    private void tryLanguage(final FaceletContext ctx, UIComponent parent, String language)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException, IOException {
        TagConfig cfg = getLanguageAwareTag(language);

        Class<?> includeClass;
        if (Configuration.myFaces)
            includeClass = Class.forName("org.apache.myfaces.view.facelets.tag.ui.IncludeHandler");
        else
            includeClass = Class.forName("com.sun.faces.facelets.tag.ui.IncludeHandler");
        Constructor<?> constructor = includeClass.getConstructor(TagConfig.class);
        TagHandler worker = (TagHandler) constructor.newInstance(cfg);
        worker.apply(ctx, parent);
    }

    private TagConfig getLanguageAwareTag(final String language) {
        TagConfig cfg = new TagConfig() {

            @Override
            public String getTagId() {
                return config.getTagId();
            }

            @Override
            public Tag getTag() {
                Tag tag = config.getTag();
                TagAttribute[] newAttributes = new TagAttribute[1];
                final String filename = src.getValue();

                final String modifiedFilename;
                if (language == null || language.length() == 0)
                    modifiedFilename = filename;
                else
                    modifiedFilename = filename.replace(".xhtml", "_" + language + ".xhtml");
                newAttributes[0] = TagAttributeUtilities.createTagAttribute(tag.getLocation(), "", "src", "src",
                        modifiedFilename);
                TagAttributes modified = new AFTagAttributes(newAttributes);
                Tag newTag = new Tag(tag.getLocation(), "http://xmlns.jcp.org/jsf/html", tag.getLocalName(), tag.getQName(),
                        modified);
                return newTag;
            }

            @Override
            public FaceletHandler getNextHandler() {
                return config.getNextHandler();
            }
        };
        return cfg;
    }
}
