// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsytems;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants.ManipulatorConstants;

public class Manipulator extends SubsystemBase {
  /** Creates a new Manipulator. */
  TalonFX pivotMotor = new TalonFX(ManipulatorConstants.pivotMotorID);

  CANrange manipRange = new CANrange(ManipulatorConstants.canRangeID);

  Orchestra rickroll = new Orchestra();

  private ManipulatorFeeder manipfeed = new ManipulatorFeeder();
  private TalonFX feederMotor = manipfeed.manipFeeder;

  TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
  MotionMagicConfigs pivotMagic = new MotionMagicConfigs();
  CANrangeConfiguration rangeConfig = new CANrangeConfiguration();

  MotionMagicVoltage magicRequest = new MotionMagicVoltage(0).withSlot(0);
  VoltageOut m_request = new VoltageOut(0);

  public Manipulator() {
    pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    pivotConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    pivotConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
        ManipulatorConstants.climberForwardSoftLimit;
    pivotConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
        ManipulatorConstants.climberReverseSoftLimit;
    pivotConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    pivotConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    rangeConfig.ProximityParams.ProximityThreshold = ManipulatorConstants.kCanRangeDistance;

    var slot0Configs = new Slot0Configs();
    slot0Configs.kG = ManipulatorConstants.kG;
    slot0Configs.kV = ManipulatorConstants.kV;
    slot0Configs.kP = ManipulatorConstants.kP;
    slot0Configs.kI = ManipulatorConstants.kI;
    slot0Configs.kD = ManipulatorConstants.kD;
    slot0Configs.GravityType = GravityTypeValue.Arm_Cosine;

    pivotMagic.MotionMagicAcceleration = ManipulatorConstants.kMagicAcceleration;
    pivotMagic.MotionMagicCruiseVelocity = ManipulatorConstants.kMagicVelocity;

    manipRange.getConfigurator().apply(rangeConfig);
    pivotMotor.getConfigurator().apply(pivotConfig);
    pivotMotor.getConfigurator().apply(slot0Configs);
    pivotMotor.getConfigurator().apply(pivotMagic);

    rickroll.addInstrument(pivotMotor);
    rickroll.addInstrument(feederMotor);
    rickroll.loadMusic("music/rickroll.chrp");
  }

  public double getPosition() {
    return pivotMotor.getPosition().getValueAsDouble();
  }

  public void goToSetpoint(double setpoint) {
    pivotMotor.setControl(magicRequest.withPosition(setpoint));
    rickroll.play();
  }

  public boolean atSetpoint(double setpoint, double deadzone) {
    return pivotMotor.getPosition().getValueAsDouble() >= setpoint - deadzone
        && pivotMotor.getPosition().getValueAsDouble() <= setpoint + deadzone;
  }

  public void zeroManip() {
    pivotMotor.setPosition(0);
  }

  public boolean laserPassed() {
    return manipRange.getIsDetected().getValue();
  }
}
