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
import frc.robot.Constants.ManipulatorConstants;

public class Manipulator extends SubsystemBase {
  /** Creates a new Manipulator. */
  TalonFX feederMotor = new TalonFX(0);

  TalonFX pivotMotor = new TalonFX(0);
  TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
  MotionMagicConfigs pivotMagic = new MotionMagicConfigs();
  MotionMagicVoltage magicRequest = new MotionMagicVoltage(0).withSlot(0);
  VoltageOut m_requst = new VoltageOut(0);

  public Manipulator() {
    pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    pivotConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    pivotConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
        ManipulatorConstants.climberForwardSoftLimit;
    pivotConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
        ManipulatorConstants.climberReverseSoftLimit;
    pivotConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
    pivotConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

    var slot0Configs = new Slot0Configs();
    slot0Configs.kG = ManipulatorConstants.kG;
    slot0Configs.kV = ManipulatorConstants.kV;
    slot0Configs.kP = ManipulatorConstants.kP;
    slot0Configs.kI = ManipulatorConstants.kI;
    slot0Configs.kD = ManipulatorConstants.kD;
    slot0Configs.GravityType = GravityTypeValue.Arm_Cosine;

    pivotMagic.MotionMagicAcceleration = ManipulatorConstants.kMagicAcceleration;
    pivotMagic.MotionMagicCruiseVelocity = ManipulatorConstants.kMagicVelocity;

    pivotMotor.getConfigurator().apply(pivotConfig);

    pivotMotor.getConfigurator().apply(slot0Configs);
    pivotMotor.getConfigurator().apply(pivotMagic);
  }
}
