// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsytems;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.constants.Constants.ElevatorConstants;

public class Elevator extends SubsystemBase {
  // Motors
  TalonFX elevatorMaster = new TalonFX(Constants.ElevatorConstants.elevatorMasterID);
  TalonFX elevatorFollower = new TalonFX(Constants.ElevatorConstants.elevatorFollowerID);
  // Configs
  TalonFXConfiguration elevatorConfigs = new TalonFXConfiguration();
  Slot0Configs elevatorPIDConfigs = new Slot0Configs();
  MotionMagicConfigs elevatorMagicConfigs = new MotionMagicConfigs();
  // Voltages
  VoltageOut m_request = new VoltageOut(0);
  MotionMagicVoltage m_magicRequest = new MotionMagicVoltage(0);

  /** Creates a new Elevator. */
  public Elevator() {
    elevatorMaster.getConfigurator().apply(new TalonFXConfiguration());
    elevatorFollower.getConfigurator().apply(new TalonFXConfiguration());

    elevatorConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    elevatorConfigs.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    elevatorConfigs.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
        ElevatorConstants.elevatorForwardSoftLimit;
    elevatorConfigs.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
        ElevatorConstants.elevatorReverseSoftLimit;
    elevatorConfigs.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    elevatorConfigs.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    var slot0Configs = new Slot0Configs();
    slot0Configs.kG = ElevatorConstants.kG;
    slot0Configs.kV = ElevatorConstants.kV;
    slot0Configs.kP = ElevatorConstants.kP;
    slot0Configs.kI = ElevatorConstants.kI;
    slot0Configs.kD = ElevatorConstants.kD;
    slot0Configs.GravityType = GravityTypeValue.Elevator_Static;

    elevatorMagicConfigs.MotionMagicAcceleration = ElevatorConstants.kMagicAcceleration;
    elevatorMagicConfigs.MotionMagicCruiseVelocity = ElevatorConstants.kMagicVelocity;

    elevatorMaster.getConfigurator().apply(elevatorConfigs);
    elevatorFollower.getConfigurator().apply(elevatorConfigs);
    elevatorFollower.setControl(new Follower(elevatorMaster.getDeviceID(), true));

    elevatorMaster.getConfigurator().apply(slot0Configs);
    elevatorMaster.getConfigurator().apply(elevatorMagicConfigs);
  }

  public void manualElevatorMotor(double voltage) {
    elevatorMaster.setControl(m_request.withOutput(voltage));
  }

  public void stopElevatorMotor() {
    elevatorMaster.setControl(m_request.withOutput(0));
  }

  public void goToSetPoint(double setpoint) {
    elevatorMaster.setControl(m_magicRequest.withPosition(setpoint));
  }

  public void setElevatorZero() {
    elevatorMaster.setPosition(0);
    elevatorFollower.setPosition(0);
  }

  public boolean atSetpoint(double setpoint) {
    return elevatorMaster.getPosition().getValueAsDouble() >= setpoint - 0.3
        && elevatorMaster.getPosition().getValueAsDouble() <= setpoint + 0.3;
  }

  public double getPose() {
    return elevatorMaster.getPosition().getValueAsDouble();
  }

  public int elevatorState = 0;
}
