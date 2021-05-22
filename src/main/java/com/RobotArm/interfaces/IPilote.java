package com.RobotArm.interfaces;

import java.sql.SQLException;

public interface IPilote
{
	public void notifierMessage(String msg) throws SQLException;
}