package es.upm.interfaces.impl;

import es.upm.interfaces.LivingLabDevice;

public class DummyDevice implements LivingLabDevice {
    private boolean state = false;
    @Override
    public Object getDeviceState(Object device) {
        boolean copy_prev_state = state;
        state = !state;
        return copy_prev_state;
    }

    @Override
    public Object setDeviceState(Object device, Object newState) {
        return !state;
    }
}
