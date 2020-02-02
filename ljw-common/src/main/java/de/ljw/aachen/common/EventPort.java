package de.ljw.aachen.common;


public interface EventPort {

    <T> void publish(T event);

}


