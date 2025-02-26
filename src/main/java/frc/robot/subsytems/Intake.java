// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsytems;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.constants.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  TalonFX intakePIDMotor = new TalonFX(Constants.IntakeConstants.intakePIDMotorID);
  TalonFX intakeMotor = new TalonFX(Constants.IntakeConstants.intakeMotorID);
  DigitalInput beamBreak = new DigitalInput(Constants.IntakeConstants.BeamBreakID);

  MotionMagicVoltage m_MagicRequest = new MotionMagicVoltage(0).withSlot(0);
  VoltageOut m_VoltageOut = new VoltageOut(0);

  TalonFXConfiguration intakeConfigs = new TalonFXConfiguration();
  Slot0Configs intakePIDConfigs = new Slot0Configs();
  MotionMagicConfigs intakeMagicConfigs = new MotionMagicConfigs();

  /** Creates a new Intake. */
  public Intake() {
    intakeConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    intakeConfigs.SoftwareLimitSwitch.withForwardSoftLimitThreshold(0);
    intakeConfigs.SoftwareLimitSwitch.withForwardSoftLimitEnable(true);
    intakeConfigs.SoftwareLimitSwitch.withReverseSoftLimitThreshold(0);
    intakeConfigs.SoftwareLimitSwitch.withReverseSoftLimitEnable(true);

    var slot0Configs = new Slot0Configs();
    slot0Configs.kG = IntakeConstants.intakeKG;
    slot0Configs.kV = IntakeConstants.intakeKV;
    slot0Configs.kP = IntakeConstants.intakeKP;
    slot0Configs.kI = IntakeConstants.intakeKI;
    slot0Configs.kD = IntakeConstants.intakeKD;
    slot0Configs.GravityType = GravityTypeValue.Arm_Cosine;

    intakePIDMotor.getConfigurator().apply(intakeMagicConfigs);
    intakePIDMotor.getConfigurator().apply(intakePIDConfigs);
    intakePIDMotor.getConfigurator().apply(intakeConfigs);

    intakeMotor.getConfigurator().apply(intakeConfigs);
  }

  public boolean beamBreakTriggered() {
    return !beamBreak.get();
  }

  public void runFlywheel(double voltage) {
    intakeMotor.setControl(m_VoltageOut.withOutput(voltage));
  }

  public void reverseFlywheel(double voltage) {
    intakeMotor.setControl(m_VoltageOut.withOutput(-voltage));
  }

  public void resetFlywheel() {
    intakeMotor.setControl(m_VoltageOut.withOutput(0));
  }

  public boolean isAtSetpoint(double setpoint) {
    return intakePIDMotor.getPosition().getValueAsDouble() == setpoint;
  }

  public void goToSetpoint(double setpoint) {
    intakePIDMotor.setControl(m_MagicRequest.withPosition(setpoint));
  }

  public double getPosition() {
    return intakePIDMotor.getPosition().getValueAsDouble();
  }

  public void zeroIntakePivot() {
    intakePIDMotor.setPosition(0);
  }

  public void resetPIDMotor() {
    intakePIDMotor.setControl(m_VoltageOut.withOutput(0));
  }
}
