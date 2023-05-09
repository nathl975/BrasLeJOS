package com.RobotArm.interfaces;

import com.RobotArm.business.Gamme;

import java.lang.reflect.Type;

public interface IAdapter<T> {
    String serialize(T object);

    Gamme deserialize(String json, Type type);
}
