package gr.escsoft.michaelkeskinidis.weatherforecast.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by Michael on 8/13/2015.
 *
 * The singleton instance for obtaining the bus
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
