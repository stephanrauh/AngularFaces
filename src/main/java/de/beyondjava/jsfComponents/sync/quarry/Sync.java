package de.beyondjava.jsfComponents.sync.quarry;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

//@FacesComponent("de.beyondjava.Sync")
//@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
//      @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Sync extends UICommand implements org.primefaces.component.api.AjaxSource {

   protected enum PropertyKeys {

      async, autoRun, global, ignoreAutoUpdate, name, oncomplete, onerror, onstart, onsuccess, partialSubmit, process, resetValues, update;

      String toString;

      PropertyKeys() {
      }

      PropertyKeys(String toString) {
         this.toString = toString;
      }

      @Override
      public String toString() {
         return ((this.toString != null) ? this.toString : super.toString());
      }
   }

   public static final String COMPONENT_FAMILY = "org.primefaces.component";
   public static final String COMPONENT_TYPE = "de.beyondjava.Sync";

   private static final String DEFAULT_RENDERER = "de.beyondjava.sync";

   public Sync() {
      setRendererType(DEFAULT_RENDERER);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   public java.lang.String getName() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.name, null);
   }

   @Override
   public java.lang.String getOncomplete() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
   }

   @Override
   public java.lang.String getOnerror() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.onerror, null);
   }

   @Override
   public java.lang.String getOnstart() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
   }

   @Override
   public java.lang.String getOnsuccess() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.onsuccess, null);
   }

   @Override
   public java.lang.String getProcess() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
   }

   @Override
   protected Renderer getRenderer(FacesContext arg0) {
      return new SyncRenderer();
   }

   @Override
   public java.lang.String getUpdate() {
      return "@this";
   }

   @Override
   public boolean isAjaxified() {
      return true;
   }

   @Override
   public boolean isAsync() {
      return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.async, false);
   }

   public boolean isAutoRun() {
      return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoRun, false);
   }

   @Override
   public boolean isGlobal() {
      return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
   }

   @Override
   public boolean isIgnoreAutoUpdate() {
      return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.ignoreAutoUpdate, false);
   }

   @Override
   public boolean isPartialSubmit() {
      return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
   }

   @Override
   public boolean isPartialSubmitSet() {
      return (getStateHelper().get(PropertyKeys.partialSubmit) != null)
            || (this.getValueExpression("partialSubmit") != null);
   }

   @Override
   public boolean isResetValues() {
      return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resetValues, false);
   }

   @Override
   public boolean isResetValuesSet() {
      return (getStateHelper().get(PropertyKeys.resetValues) != null)
            || (this.getValueExpression("resetValues") != null);
   }

   public void setAsync(boolean _async) {
      getStateHelper().put(PropertyKeys.async, _async);
   }

   public void setAutoRun(boolean _autoRun) {
      getStateHelper().put(PropertyKeys.autoRun, _autoRun);
   }

   public void setGlobal(boolean _global) {
      getStateHelper().put(PropertyKeys.global, _global);
   }

   public void setIgnoreAutoUpdate(boolean _ignoreAutoUpdate) {
      getStateHelper().put(PropertyKeys.ignoreAutoUpdate, _ignoreAutoUpdate);
   }

   public void setName(java.lang.String _name) {
      getStateHelper().put(PropertyKeys.name, _name);
   }

   public void setOncomplete(java.lang.String _oncomplete) {
      getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
   }

   public void setOnerror(java.lang.String _onerror) {
      getStateHelper().put(PropertyKeys.onerror, _onerror);
   }

   public void setOnstart(java.lang.String _onstart) {
      getStateHelper().put(PropertyKeys.onstart, _onstart);
   }

   public void setOnsuccess(java.lang.String _onsuccess) {
      getStateHelper().put(PropertyKeys.onsuccess, _onsuccess);
   }

   public void setPartialSubmit(boolean _partialSubmit) {
      getStateHelper().put(PropertyKeys.partialSubmit, _partialSubmit);
   }

   public void setProcess(java.lang.String _process) {
      getStateHelper().put(PropertyKeys.process, _process);
   }

   public void setResetValues(boolean _resetValues) {
      getStateHelper().put(PropertyKeys.resetValues, _resetValues);
   }

   public void setUpdate(java.lang.String _update) {
      getStateHelper().put(PropertyKeys.update, _update);
   }

}