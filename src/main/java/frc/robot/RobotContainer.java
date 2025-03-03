// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import dev.doglog.DogLog;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.Climber.runClimber;
import frc.robot.commands.Elevator.algaeFlick;
import frc.robot.commands.Elevator.autoElevator;
import frc.robot.commands.Elevator.runElevator;
import frc.robot.commands.Intake.runIntake;
import frc.robot.commands.Intake.runIntakeTrough;
import frc.robot.commands.Manipulator.autoManipIntake;
import frc.robot.commands.Manipulator.manipIntake;
import frc.robot.commands.Manipulator.manipOuttake;
import frc.robot.commands.Manipulator.manipPivot;
import frc.robot.commands.drivetrain.ReefAlign;
import frc.robot.constants.Constants.ClimberConstants;
import frc.robot.constants.Constants.ElevatorConstants;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.constants.FieldConstants;
import frc.robot.constants.TunerConstants;
import frc.robot.subsytems.Climber;
import frc.robot.subsytems.CommandSwerveDrivetrain;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Intake;
import frc.robot.subsytems.Manipulator;
import util.AllianceFlipUtil;
import vision.LimelightHelpers;

public class RobotContainer {
  private Climber s_Climber = new Climber();
  private Elevator s_Elevator = new Elevator();
  private Intake s_Intake = new Intake();
  private Manipulator s_Manipulator = new Manipulator();
  private CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  public CommandXboxController driverCon = new CommandXboxController(0);
  public CommandXboxController opCon = new CommandXboxController(1);
  public double lastTagID;

  private static double MaxSpeed =
      TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
  private static double MaxAngularRate =
      RotationsPerSecond.of(0.75)
          .in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
  private static final Telemetry logger = new Telemetry(MaxSpeed);

  private final SendableChooser<Command> auto;

  /* Setting up bindings for necessary control of the swerve drive platform */

  public RobotContainer() {
    driverControls();
    opControls();

    drivetrain.configureAutoBuilder();

    // Setting the Trough to work with Auto

    // Setting the Elevator to work with Autos

    // Setting the Manip Commands to work with Autos
    NamedCommands.registerCommand("ManipOuttake", new manipOuttake(s_Manipulator));
    NamedCommands.registerCommand("intakeManip", new autoManipIntake(s_Manipulator));
    NamedCommands.registerCommand("moveAlgaeUp", new manipPivot(s_Manipulator, 0, false));
    NamedCommands.registerCommand("moveAlgaeDown", new manipPivot(s_Manipulator, 5.5, false));
    NamedCommands.registerCommand("Trough", new runIntakeTrough(s_Intake, -1.5));
    NamedCommands.registerCommand("L2", new manipPivot(s_Manipulator, 0, false));
    NamedCommands.registerCommand(
        "L3", new autoElevator(s_Elevator, ElevatorConstants.L3Setpoint, s_Manipulator));

    auto = AutoBuilder.buildAutoChooser();

    auto.setDefaultOption("testAuto", new PathPlannerAuto("Test Auto"));

    SmartDashboard.putData(auto);
  }

  public void driverControls() {
    drivetrain.registerTelemetry(logger::telemeterize);

    final SwerveRequest.FieldCentric drive =
        new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1)
            .withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(
                DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    drivetrain.setDefaultCommand(
        // drivetrain will execute this command periodically
        drivetrain.applyRequest(
            () ->
                drive
                    .withVelocityX(
                        -driverCon.getLeftY()
                            * MaxSpeed
                            * (driverCon.getRightTriggerAxis() > 0.5
                                ? 0.5
                                : 1)) // Drive forward with negative Y (forward)
                    .withVelocityY(
                        -driverCon.getLeftX()
                            * MaxSpeed
                            * (driverCon.getRightTriggerAxis() > 0.5
                                ? 0.5
                                : 1)) // Drive left with negative X (left)
                    .withRotationalRate(
                        -driverCon.getRightX()
                            * MaxAngularRate) // Drive counterclockwise with negative X (left)
            ));

    driverCon.back().and(driverCon.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
    driverCon.back().and(driverCon.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
    driverCon.start().and(driverCon.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
    driverCon.start().and(driverCon.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on y button press
    driverCon.y().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

    /* Other Subsystems Declairations */

    // Buttons to make Manip work
    driverCon.rightBumper().whileTrue(new manipIntake(s_Manipulator));

    // Trough Shot Button
    driverCon.a().whileTrue(new runIntakeTrough(s_Intake, -2));

    // Command to run the intake const and make it work
    s_Intake.setDefaultCommand(new runIntake(s_Intake, driverCon.leftTrigger(0.5), 4.0));

    driverCon
        .povLeft()
        .onTrue(
            Commands.runOnce(() -> lastTagID = LimelightHelpers.getFiducialID("limelight-front")));
    driverCon
        .povRight()
        .onTrue(
            Commands.runOnce(() -> lastTagID = LimelightHelpers.getFiducialID("limelight-front")));

    driverCon
        .povLeft()
        .and(
            () -> {
              var validTags =
                  AllianceFlipUtil.get(
                      FieldConstants.BLUE_VALID_REEF_TAGS, FieldConstants.RED_VALID_REEF_TAGS);
              return validTags.contains((int) lastTagID);
            })
        .whileTrue(new ReefAlign(drivetrain, true));

    driverCon
        .y()
        .and(
            () -> {
              var validTags =
                  AllianceFlipUtil.get(
                      FieldConstants.BLUE_VALID_REEF_TAGS, FieldConstants.RED_VALID_REEF_TAGS);
              return validTags.contains((int) lastTagID);
            })
        .whileTrue(new ReefAlign(drivetrain, false));
  }

  public void opControls() {
    /* Without eSwitch */

    opCon.leftBumper();

    // Run Climber When Pushed
    opCon
        .rightBumper()
        .whileTrue(new runClimber(s_Climber, ClimberConstants.setpointForClimb))
        .whileFalse(new runClimber(s_Climber, 0.0));

    // Go To Elevator setpoints When Pushed
    opCon
        .povUp()
        .onTrue(
            new runElevator(
                s_Elevator,
                ElevatorConstants.L3Setpoint,
                s_Manipulator,
                5.4,
                driverCon.rightBumper()));

    opCon
        .povLeft()
        .whileTrue(new manipPivot(s_Manipulator, 5, true))
        .whileFalse(new manipPivot(s_Manipulator, 0, false));

    opCon
        .povRight()
        .onTrue(new runElevator(s_Elevator, 20, s_Manipulator, 5.4, driverCon.rightBumper()));

    opCon.povDown().whileTrue(new algaeFlick(s_Elevator, s_Manipulator, s_Intake, false));

    opCon
        .povDown()
        .and(opCon.start())
        .whileTrue(new algaeFlick(s_Elevator, s_Manipulator, s_Intake, true));

    /* With eSwitch */

    // Zero Subsystems When Pushed
    opCon.x().whileTrue(Commands.runOnce(() -> s_Climber.zeroClimber()));
    opCon.b().whileTrue(Commands.runOnce(() -> s_Elevator.setElevatorZero()));
    opCon.leftTrigger().whileTrue(Commands.runOnce(() -> s_Intake.zeroIntakePivot()));
    // opCon.rightBumper().whileTrue(Commands.runOnce(() -> s_Manipulator.zeroManip()));
    //  opCon.leftBumper().onTrue(new reefAlignForAlgae(drivetrain, false, Elevator,
    // s_Manipulator));

    opCon
        .rightTrigger(0.5)
        .whileTrue(new manipPivot(s_Manipulator, ManipulatorConstants.manipSetpoint, false))
        .whileFalse(new manipPivot(s_Manipulator, 0, false));
  }

  public void RobotContainerPeriodic() {
    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());
    SmartDashboard.putNumber("Manip Pose", s_Manipulator.getPose());
    SmartDashboard.putNumber("Elevator Pose", s_Elevator.getPose());
    SmartDashboard.putNumber("Intake Pose", s_Intake.getPosition());
    SmartDashboard.putNumber("Climber Pose", s_Climber.getPose());
    SmartDashboard.putNumber("Intake AMP", s_Intake.getAmp());
    SmartDashboard.putNumber("Elevator State", s_Elevator.elevatorState);

    // Log Posistion For Climber, Elevator, Intake, and s_Manipulator
    DogLog.log("ClimberPos", s_Climber.getPose());
    DogLog.log("Elevator Pos", s_Elevator.getPose());
    DogLog.log("Intake Pos", s_Intake.getPosition());
    DogLog.log("s_Manipulator Pos", s_Manipulator.getPose());

    DogLog.log("Elevator State", s_Elevator.elevatorState);

    // Log Swerve
    DogLog.log("Swerve Rot", drivetrain.getOperatorForwardDirection());
  }

  public void zeros() {
    s_Intake.zeroIntakePivot();
    // s_Climber.zeroClimber();
    s_Elevator.setElevatorZero();
    s_Manipulator.zeroManip();
  }

  public Command getAutonomousCommand() {
    return auto.getSelected();
  }
}
