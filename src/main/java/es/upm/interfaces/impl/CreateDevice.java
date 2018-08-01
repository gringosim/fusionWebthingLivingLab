package es.upm.interfaces.impl;

import es.upm.interfaces.LivingLabDevice;

public class CreateDevice implements LivingLabDevice {
    private boolean status;



    @Override
    public Object getDeviceStatus(Object device) {
        boolean copy_prev_status = status;
        status = !status;
        return copy_prev_status;
    }

    @Override
    public Object setDeviceStatus(Object device, Object newStatus) {
        return !status;
    }
}
