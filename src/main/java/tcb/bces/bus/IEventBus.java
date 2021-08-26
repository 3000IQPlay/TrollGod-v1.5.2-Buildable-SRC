package tcb.bces.bus;

import tcb.bces.event.Event;
import tcb.bces.listener.IListener;

/**
 * Any event bus implements this interface
 * 
 * @author TCB
 *
 */
public interface IEventBus {
	/**
	 * Registers an {@link IListener} to this bus
	 */
    void register ( IListener listener );
	
	/**
	 * Unregisters an {@link IListener} from this bus.
	 * Only unregisters the first occurrence of the specified listener.
	 * @param listener {@link IListener} to unregister
	 */
    void unregister ( IListener listener );
	
	/**
	 * Posts an {@link Event} and returns the posted event.
	 * @param event {@link Event} to dispatch
	 * @return {@link Event} the posted event
	 */
    <T extends Event> T post ( T event );
}
