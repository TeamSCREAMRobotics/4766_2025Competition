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
import frc.robot.commands.Climber.RunClimber;
import frc.robot.commands.Drivetrain.ReefAlign;
import frc.robot.commands.Elevator.AlgaeFlick;
import frc.robot.commands.Elevator.AutoElevator;
import frc.robot.commands.Elevator.AutoFlick;
import frc.robot.commands.Elevator.RunElevator;
import frc.robot.commands.Manipulator.AutoManipIntake;
import frc.robot.commands.Manipulator.AutoScore;
import frc.robot.commands.Manipulator.ManipIdle;
import frc.robot.commands.Manipulator.ManipIntake;
import frc.robot.commands.Manipulator.ManipPivot;
import frc.robot.constants.Constants.ClimberConstants;
import frc.robot.constants.Constants.ElevatorConstants;
import frc.robot.constants.FieldConstants;
import frc.robot.constants.TunerConstants;
import frc.robot.subsytems.Climber;
import frc.robot.subsytems.CommandSwerveDrivetrain;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Manipulator;
import frc.robot.subsytems.ManipulatorFeeder;
import util.AllianceFlipUtil;
import vision.LimelightHelpers;

public class RobotContainer {
  private Climber s_Climber = new Climber();
  private Elevator s_Elevator = new Elevator();
  private Manipulator s_Manipulator = new Manipulator();
  private ManipulatorFeeder s_ManipFeed = new ManipulatorFeeder();
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

    // LimelightHelpers.SetRobotOrientation("limelight-front", -160, 0.0, 20.0, 0.0, 0.0, 0.0);
    drivetrain.configureAutoBuilder();

    // Setting the Trough to work with Auto

    // Setting the Elevator to work with Autos

    // Setting the Manip Commands to work with Autos
    NamedCommands.registerCommand("intakeManip", new AutoManipIntake(s_Manipulator, s_ManipFeed));
    NamedCommands.registerCommand("AlgaeFlickL2", new AutoFlick(s_Elevator, s_Manipulator, false));
    NamedCommands.registerCommand("AlgaeFlickL3", new AutoFlick(s_Elevator, s_Manipulator, true));
    NamedCommands.registerCommand("moveAlgaeUp", new ManipPivot(s_Manipulator, 0, false));
    NamedCommands.registerCommand("L2", new AutoScore(s_Manipulator, s_ManipFeed));
    NamedCommands.registerCommand(
        "L3", new AutoElevator(s_Elevator, ElevatorConstants.L3Setpoint, s_Manipulator));

    auto = AutoBuilder.buildAutoChooser();

    auto.setDefaultOption("testAuto", new PathPlannerAuto("Test Auto"));

    SmartDashboard.putData(auto);
  }

  public void zeroSwerve() {
    drivetrain.runOnce(() -> drivetrain.seedFieldCentric());
  }

  public void driverControls() {
    drivetrain.registerTelemetry(logger::telemeterize);

    final SwerveRequest.FieldCentric drive =
        new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.15)
            .withRotationalDeadband(MaxAngularRate * 0.15) // Add a 10% deadband
            .withDriveRequestType(
                DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    drivetrain.setDefaultCommand(
        // drivetrain will execute this command periodically
        drivetrain.applyRequest(
            () ->
                drive
                    .withVelocityX(
                        -driverCon.getLeftY()
                            * 1.2
                            * (driverCon.getRightTriggerAxis() > 0.5
                                ? MaxSpeed
                                : 1)) // Drive forward with negative Y (forward)
                    .withVelocityY(
                        -driverCon.getLeftX()
                            * 1.2
                            * (driverCon.getRightTriggerAxis() > 0.5
                                ? MaxSpeed
                                : 1)) // Drive left with negative X (left)
                    .withRotationalRate(
                        -driverCon.getRightX()
                            * MaxAngularRate) // Drive counterclockwise with negative X (left)
            ));
    // .alongWith(Commands.run(() -> drivetrain.addVision())));

    driverCon.back().and(driverCon.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
    driverCon.back().and(driverCon.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
    driverCon.start().and(driverCon.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
    driverCon.start().and(driverCon.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on y button press
    driverCon.y().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

    /* Other Subsystems Declairations */

    // Buttons to make Manip work
    driverCon.rightBumper().whileTrue(new ManipIntake(s_Manipulator, s_Elevator, s_ManipFeed));

    // Trough Shot Button

    // Command to run the intake const and make it work

    s_Manipulator.setDefaultCommand(new ManipIdle(s_ManipFeed));

    driverCon
        .povLeft()
        .onTrue(
            Commands.runOnce(() -> lastTagID = LimelightHelpers.getFiducialID("limelight-front")));
    driverCon
        .povRight()
        .onTrue(
            Commands.runOnce(() -> lastTagID = LimelightHelpers.getFiducialID("limelight-front")));

    // TODO: FIX: Left and right are flipped on auto align.
    driverCon
        .povLeft()
        .and(
            () -> {
              var validTags =
                  AllianceFlipUtil.get(
                      FieldConstants.BLUE_VALID_REEF_TAGS, FieldConstants.RED_VALID_REEF_TAGS);
              return validTags.contains((int) lastTagID);
            })
        .whileTrue(new ReefAlign(drivetrain, false));

    driverCon
        .povRight()
        .and(
            () -> {
              var validTags =
                  AllianceFlipUtil.get(
                      FieldConstants.BLUE_VALID_REEF_TAGS, FieldConstants.RED_VALID_REEF_TAGS);
              return validTags.contains((int) lastTagID);
            })
        .whileTrue(new ReefAlign(drivetrain, true));

    driverCon.start().whileTrue(new AutoElevator(s_Elevator, 17.5, s_Manipulator));
  }

  public void opControls() {
    /* Without eSwitch */
    opCon.leftBumper();

    // Run Climber When Pushed
    opCon
        .rightBumper()
        .whileTrue(new RunClimber(s_Climber, ClimberConstants.setpointForClimb))
        .whileFalse(new RunClimber(s_Climber, 0.0));

    // Go To Elevator setpoints When Pushed
    opCon
        .povUp()
        .onTrue(
            new RunElevator(
                s_Elevator,
                ElevatorConstants.L3Setpoint,
                s_Manipulator,
                5.4,
                driverCon.rightBumper()));
    //  L2 no Elevator.
    opCon
        .povLeft()
        .whileTrue(new ManipPivot(s_Manipulator, 4.75, true))
        .whileFalse(new ManipPivot(s_Manipulator, 0, false));

    opCon.povDown().whileTrue(new AlgaeFlick(s_Elevator, s_Manipulator, false, opCon.povDown()));

    opCon
        .povDown()
        .and(opCon.start())
        .whileTrue(new AlgaeFlick(s_Elevator, s_Manipulator, true, opCon.povDown()));

    /* With eSwitch */

    // Zero Subsystems When Pushed
    opCon.x().and(opCon.start()).whileTrue(Commands.runOnce(() -> s_Climber.zeroClimber()));
    opCon.b().and(opCon.start()).whileTrue(Commands.runOnce(() -> s_Elevator.setElevatorZero()));
    opCon.y().and(opCon.start()).whileTrue(Commands.runOnce(() -> s_Manipulator.zeroManip()));
  }

  public void RobotContainerPeriodic() {
    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());
    SmartDashboard.putNumber("Manip Pose", s_Manipulator.getPosition());
    SmartDashboard.putNumber("Elevator Pose", s_Elevator.getPosition());
    SmartDashboard.putNumber("Climber Pose", s_Climber.getPosition());
    SmartDashboard.putNumber("Elevator State", s_Elevator.elevatorState);

    // Log Posistion For Climber, Elevator, Intake, and s_Manipulator
    DogLog.log("ClimberPos", s_Climber.getPosition());
    DogLog.log("Elevator Pos", s_Elevator.getPosition());
    DogLog.log("s_Manipulator Pos", s_Manipulator.getPosition());
    DogLog.log("Elevator State", s_Elevator.elevatorState);

    // Log Swerve
    DogLog.log("Swerve Rot", drivetrain.getOperatorForwardDirection());
    DogLog.log("Swerve Rotation3d", drivetrain.getRotation3d());
    DogLog.log("Swerve States", drivetrain.getModuleStates());
    DogLog.log("Swerve Speed", drivetrain.getFieldVelocity());
  }

  public void zeros() {
    // s_Climber.zeroClimber();
    s_Elevator.setElevatorZero();
    // s_Manipulator.zeroManip();
  }

  public Command getAutonomousCommand() {
    return auto.getSelected();
  }
}
