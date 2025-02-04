// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import dev.doglog.DogLog;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.Climber.runClimber;
import frc.robot.controls.Controls;
import frc.robot.subsytems.Climber;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Intake;
import frc.robot.subsytems.Manipulator;

public class RobotContainer {
  private Climber s_Climber = new Climber();
  private Elevator s_Elevator = new Elevator();
  private Intake s_Intake = new Intake();
  private Manipulator s_Manipulator = new Manipulator();

  
  public RobotContainer() {
    DogLog.log("ClimberPos", s_Climber.ClimberPos());

    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());

    configureBindings();
    Controls.driverControls();
    Controls.opControls();
    Controls.buttonBoard();
  }

  private void configureBindings() {
    Controls.driverCon.a().whileTrue(new runClimber(s_Climber, 8.0));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
