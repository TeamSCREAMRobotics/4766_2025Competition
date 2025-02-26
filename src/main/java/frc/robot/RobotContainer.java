// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import dev.doglog.DogLog;
import dev.doglog.DogLogOptions;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Elevator.runElevator;
import frc.robot.commands.Intake.intakeSpitOut;
import frc.robot.commands.Manipulator.manipIntake;
import frc.robot.commands.Manipulator.manipOuttake;
import frc.robot.commands.Manipulator.manipPivot;
import frc.robot.constants.Constants.ElevatorConstants;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.constants.TunerConstants;
import frc.robot.controls.Controls;
import frc.robot.subsytems.Climber;
import frc.robot.subsytems.CommandSwerveDrivetrain;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Intake;
import frc.robot.subsytems.Manipulator;

public class RobotContainer {
  private Climber s_Climber = new Climber();
  private Elevator s_Elevator = new Elevator();
  private Intake s_Intake = new Intake();
  private Manipulator s_Manipulator = new Manipulator();
  private CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  private double MaxSpeed =
      TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
  private double MaxAngularRate =
      RotationsPerSecond.of(0.75)
          .in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
  private final Telemetry logger = new Telemetry(MaxSpeed);

  double Vel;
  double Rot;

  private final SendableChooser<Command> auto;

  /* Setting up bindings for necessary control of the swerve drive platform */

  public RobotContainer() {
    System.out.println(MaxSpeed);

    // Setting the Trough to work with Autos
    NamedCommands.registerCommand("Trough", new intakeSpitOut(s_Intake, 0));

    // Setting the Elevator to work with Autos
    NamedCommands.registerCommand(
        "L2",
        new runElevator(s_Elevator, ElevatorConstants.L2Setpoint)
            .alongWith(new manipPivot(s_Manipulator, ManipulatorConstants.manipSetpoint)));
    NamedCommands.registerCommand(
        "L3",
        new runElevator(s_Elevator, ElevatorConstants.L3Setpoint)
            .alongWith(new manipPivot(s_Manipulator, ManipulatorConstants.manipSetpoint)));

    // Setting the Manip Commands to work with Autos
    NamedCommands.registerCommand("ManipIntake", new manipIntake(s_Manipulator));
    NamedCommands.registerCommand("ManipOuttake", new manipOuttake(s_Manipulator));
    NamedCommands.registerCommand(
        "MaipPivot", new manipPivot(s_Manipulator, ManipulatorConstants.manipSetpoint));

    auto = AutoBuilder.buildAutoChooser();

    auto.addOption("testAuto", new PathPlannerAuto("Test Auto"));

    SmartDashboard.putData(auto);

    drivetrain.registerTelemetry(logger::telemeterize);

    DogLog.setOptions(new DogLogOptions().withCaptureDs(true));

    // Log Posistion For Climber, Elevator, Intake, and Manipulator
    DogLog.log("ClimberPos", s_Climber.getPose());
    DogLog.log("Elevator Pos", s_Elevator.getPose());
    DogLog.log("Intake Pos", s_Intake.getPosition());
    DogLog.log("Manipulator Pos", s_Manipulator.getPose());

    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());

    if (Controls.driverCon.getRightTriggerAxis() > 0.5) {
      Vel = 0.9;
      Rot = 0.99;
    } else {
      Vel = MaxSpeed;
      Rot = MaxAngularRate;
    }

    Controls.driverControls(s_Intake, s_Climber, s_Elevator, s_Manipulator, drivetrain, Vel, Rot);
    Controls.opControls(s_Intake, s_Climber, s_Elevator, s_Manipulator, drivetrain);
  }

  public Command getAutonomousCommand() {
    return auto.getSelected();
  }
}
