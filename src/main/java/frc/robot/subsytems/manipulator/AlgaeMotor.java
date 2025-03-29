// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsytems.manipulator;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants.ManipulatorConstants;

public class AlgaeMotor extends SubsystemBase {
  SparkMax algaeMax = new SparkMax(ManipulatorConstants.algaeMaxID, MotorType.kBrushless);

  /** Creates a new AlgaeMotor. */
  public AlgaeMotor() {}

  public void runAlgaeMotor(double voltage) {
    algaeMax.set(voltage);
  }

  public Command runAlgaeMotor() {
    return this.startEnd(() -> this.runAlgaeMotor(-1.5), () -> this.runAlgaeMotor(0));
  }

  public Command stopAlgaeMotor() {
    return this.run(() -> this.runAlgaeMotor(0));
  }
}
