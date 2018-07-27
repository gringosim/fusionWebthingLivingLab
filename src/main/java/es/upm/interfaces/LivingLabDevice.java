package es.upm.interfaces;

public interface LivingLabDevice {
    public Object getDeviceState(Object device);
    public Object setDeviceState(Object device,Object newState);
}
