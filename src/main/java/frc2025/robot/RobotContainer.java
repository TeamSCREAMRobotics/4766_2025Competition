// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc2025.robot;

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
import frc2025.robot.commands.AutoLevelFour;
import frc2025.robot.commands.AutoLoad;
import frc2025.robot.commands.Drivetrain.ReefAlign;
import frc2025.robot.commands.ManipIntake;
import frc2025.robot.commands.RunClimber;
import frc2025.robot.constants.Constants.ClimberConstants;
import frc2025.robot.constants.Constants.ElevatorConstants;
import frc2025.robot.constants.Constants.ManipulatorConstants;
import frc2025.robot.constants.FieldConstants;
import frc2025.robot.constants.TunerConstants;
import frc2025.robot.subsytems.Climber;
import frc2025.robot.subsytems.Elevator;
import frc2025.robot.subsytems.drivetrain.CommandSwerveDrivetrain;
import frc2025.robot.subsytems.manipulator.AlgaeMotor;
import frc2025.robot.subsytems.manipulator.Manipulator;
import frc2025.robot.subsytems.manipulator.ManipulatorFeeder;
import util.AllianceFlipUtil;
import vision.LimelightHelpers;

public class RobotContainer {
  private Climber s_Climber = new Climber();
  private Elevator s_Elevator = new Elevator();
  private Manipulator s_Manipulator = new Manipulator();
  private ManipulatorFeeder s_ManipFeed = new ManipulatorFeeder();
  private AlgaeMotor s_AlgaeMotor = new AlgaeMotor();
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
    defaultCommands();

    // LimelightHelpers.SetRobotOrientation("limelight-front", -160, 0.0, 20.0, 0.0, 0.0, 0.0);
    drivetrain.configureAutoBuilder();

    NamedCommands.registerCommand(
        "ManipHome", s_Manipulator.goDirectToSetpoint(ManipulatorConstants.clearZoneSetpoint));
    NamedCommands.registerCommand("elevatorHome", s_Elevator.toSetpoint(0));
    NamedCommands.registerCommand(
        "LevelTwo", s_Manipulator.goDirectToSetpoint(ManipulatorConstants.levelTwoSetpoint));
    NamedCommands.registerCommand(
        "LevelThree", s_Manipulator.goDirectToSetpoint(ManipulatorConstants.levelThreeSetpoint));
    NamedCommands.registerCommand(
        "levelFour", new AutoLevelFour(s_Elevator, s_Manipulator, s_ManipFeed));
    NamedCommands.registerCommand(
        "algaeFlickTwo",
        s_AlgaeMotor
            .runAlgaeMotor()
            .alongWith(
                s_Manipulator.goDirectToSetpoint(ManipulatorConstants.algaeRemovalSetpoint)));
    NamedCommands.registerCommand(
        "algaeFlickThree",
        s_AlgaeMotor
            .runAlgaeMotor()
            .alongWith(
                s_Elevator
                    .toSetpoint(ElevatorConstants.algaeFlickL3)
                    .alongWith(
                        s_Manipulator.goDirectToSetpoint(
                            ManipulatorConstants.algaeRemovalSetpoint))));

    NamedCommands.registerCommand("load", new AutoLoad(s_Manipulator, s_Elevator, s_ManipFeed));
    NamedCommands.registerCommand(
        "feedFoward", Commands.run(() -> s_ManipFeed.idleFeed(), s_ManipFeed));

    auto = AutoBuilder.buildAutoChooser();

    auto.setDefaultOption("testAuto", new PathPlannerAuto("Test Auto"));

    SmartDashboard.putData(auto);
  }

  public void zeroSwerve() {
    drivetrain.runOnce(() -> drivetrain.seedFieldCentric());
  }

  public void defaultCommands() {
    s_ManipFeed.setDefaultCommand(Commands.run(() -> s_ManipFeed.idleFeed(), s_ManipFeed));
  }

  public void driverControls() {
    drivetrain.registerTelemetry(logger::telemeterize);

    final SwerveRequest.FieldCentric drive =
        new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.10)
            .withRotationalDeadband(MaxAngularRate * 0.10) // Add a 10% deadband
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

    driverCon.back().and(driverCon.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
    driverCon.back().and(driverCon.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
    driverCon.start().and(driverCon.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
    driverCon.start().and(driverCon.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on y button press
    driverCon.y().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

    driverCon.rightBumper().whileTrue(new ManipIntake(s_Manipulator, s_Elevator, s_ManipFeed));

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
  }

  public void opControls() {
    // Run Climber When Pushed
    opCon
        .back()
        .toggleOnTrue(
            new RunClimber(
                s_Climber, s_Manipulator, ClimberConstants.setpointForClimb, opCon.rightBumper()));

    opCon
        .povDown()
        .whileTrue(
            s_AlgaeMotor
                .runAlgaeMotor()
                .alongWith(
                    s_Manipulator.goToSetpointCommand(
                        ManipulatorConstants.algaeRemovalSetpoint,
                        ManipulatorConstants.clearZoneSetpoint)));
    opCon
        .povDown()
        .and(opCon.start())
        .whileTrue(
            s_AlgaeMotor
                .runAlgaeMotor()
                .alongWith(
                    s_Elevator
                        .goDirectTOSetpoint(ElevatorConstants.algaeFlickL3)
                        .alongWith(
                            s_Manipulator.goToSetpointCommand(
                                ManipulatorConstants.algaeRemovalSetpoint,
                                ManipulatorConstants.clearZoneSetpoint))));

    opCon
        .povLeft()
        .whileTrue(
            s_Manipulator.goToSetpointCommand(
                ManipulatorConstants.levelTwoSetpoint, ManipulatorConstants.clearZoneSetpoint));

    opCon
        .povUp()
        .whileTrue(
            s_Manipulator.goToSetpointCommand(
                ManipulatorConstants.levelThreeSetpoint, ManipulatorConstants.clearZoneSetpoint));

    // TODO: this broke most likely
    opCon
        .povRight()
        .whileTrue(
            Commands.sequence(
                Commands.parallel(
                        s_Elevator.goDirectTOSetpoint(ElevatorConstants.L4Setpoint),
                        s_Manipulator.goDirectToSetpoint(ManipulatorConstants.clearZoneSetpoint))
                    .until(() -> s_Elevator.atSetpoint(ElevatorConstants.L4Setpoint, .2)),
                Commands.parallel(
                    s_Manipulator.goToSetpointCommand(
                        ManipulatorConstants.levelFourSetpoint,
                        ManipulatorConstants.clearZoneSetpoint))));

    opCon
        .leftTrigger()
        .toggleOnFalse(
            s_Elevator
                .goDirectTOSetpoint(ElevatorConstants.loadingSetpoint)
                .alongWith(
                    s_Manipulator.goToSetpointCommand(-7, ManipulatorConstants.clearZoneSetpoint)));

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

    SmartDashboard.putNumber("Encoder Pose", s_Manipulator.getMagPose());

    // Log Posistion For Climber, Elevator, Intake, and s_Manipulator
    DogLog.log("ClimberPos", s_Climber.getPosition());
    DogLog.log("Elevator Pos", s_Elevator.getPosition());
    DogLog.log("s_Manipulator Pos", s_Manipulator.getPosition());

    if (s_Climber.getPosition() > 1) {
      s_Manipulator.goDirectToSetpoint(33);
      s_Elevator.goDirectTOSetpoint(0);
    }
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
