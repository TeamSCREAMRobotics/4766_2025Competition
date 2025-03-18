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
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants.ClimberConstants;

public class Climber extends SubsystemBase {
  // If this works, it will be a miracle

  /** Creates a new Climber. */

  // Creates new TalonFX Library motors
  TalonFX climberMaster = new TalonFX(ClimberConstants.climberMasterID);

  // Configuration For The Motors
  TalonFXConfiguration climberConfig = new TalonFXConfiguration();
  MotionMagicConfigs climberMagic = new MotionMagicConfigs();
  MotionMagicVoltage magicRequest = new MotionMagicVoltage(0);
  VoltageOut m_requst = new VoltageOut(0);
  Slot0Configs slot0Configs = new Slot0Configs();

  public Climber() {
    climberConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    climberConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    climberConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
        ClimberConstants.climberForwardSoftLimit;
    climberConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
        ClimberConstants.climberReverseSoftLimit;
    climberConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    climberConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    slot0Configs.kG = ClimberConstants.kG;
    slot0Configs.kV = ClimberConstants.kV;
    slot0Configs.kP = ClimberConstants.kP;
    slot0Configs.kI = ClimberConstants.kI;
    slot0Configs.kD = ClimberConstants.kD;
    slot0Configs.GravityType = GravityTypeValue.Arm_Cosine;

    climberMagic.MotionMagicAcceleration = ClimberConstants.kMagicAcceleration;
    climberMagic.MotionMagicCruiseVelocity = ClimberConstants.kMagicVelocity;

    climberMaster.getConfigurator().apply(climberMagic);
    climberMaster.getConfigurator().apply(slot0Configs);
  }

  public void goToSetPoint(double setpoint) {
    climberMaster.setControl(magicRequest.withPosition(setpoint));
  }

  public boolean isAtSetPoint(double setpoint) {
    return climberMaster.getPosition().getValueAsDouble() >= setpoint - 0.3
        && climberMaster.getPosition().getValueAsDouble() <= setpoint + 0.3;
  }

  public double getPosition() {
    return climberMaster.getPosition().getValueAsDouble();
  }

  public void manualClimb(double voltage) {
    climberMaster.setControl(m_requst.withOutput(voltage));
  }

  public void resetManualClimb() {
    climberMaster.setControl(m_requst.withOutput(0));
  }

  public double manualClimbSpeed() {
    return climberMaster.getVelocity().getValueAsDouble();
  }

  public void zeroClimber() {
    climberMaster.setPosition(0);
  }
}
