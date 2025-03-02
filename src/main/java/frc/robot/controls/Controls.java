package frc.robot.controls;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Telemetry;
import frc.robot.commands.Climber.runClimber;
import frc.robot.commands.Elevator.runElevator;
import frc.robot.commands.Intake.runIntake;
import frc.robot.commands.Intake.runIntakeTrough;
import frc.robot.commands.Manipulator.manipIntake;
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

public class Controls {
  public static Buttonboard buttonboard = new Buttonboard(0);
  public static CommandXboxController driverCon = new CommandXboxController(1);
  public static CommandXboxController opCon = new CommandXboxController(2);
  public static double lastTagID;

  private static double MaxSpeed =
      TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
  private static double MaxAngularRate =
      RotationsPerSecond.of(0.75)
          .in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
  private static final Telemetry logger = new Telemetry(MaxSpeed);

  /* Setting up bindings for necessary control of the swerve drive platform */

  // public static final Trigger eSwitch() {
  //  return new Trigger(() -> buttonboard.getRawSwitch(8));
  // }
  //
  // public static final Trigger goToLeft() {
  //  return new Trigger(
  //      () ->
  //          buttonboard.getRawButton(1) && eSwitch().getAsBoolean() == false
  //              || opCon.leftTrigger(0.5).getAsBoolean());
  // }
  //
  // public static final Trigger goToRight() {
  //  return new Trigger(
  //      () ->
  //          buttonboard.getRawButton(2) && eSwitch().getAsBoolean() == false
  //              || opCon.rightTrigger(0.5).getAsBoolean());
  // }

  // public static final Trigger runClimber() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(3) && eSwitch().getAsBoolean() == false
  //               || opCon.rightBumper().getAsBoolean());
  // }

  // public static final Trigger zeroClimber() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(3) && eSwitch().getAsBoolean() == true
  //               || opCon.a().getAsBoolean());
  // }

  // public static final Trigger L1() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(4) && eSwitch().getAsBoolean() == false
  //               || opCon.povDown().getAsBoolean());
  // }
  //
  // public static final Trigger zeroElevator() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(4) && eSwitch().getAsBoolean() == true
  //               || opCon.b().getAsBoolean());
  // }
  //
  // public static final Trigger L2() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(5) && eSwitch().getAsBoolean() == false
  //               || opCon.povLeft().getAsBoolean());
  // }
  //
  // public static final Trigger zeroIntake() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(5) && eSwitch().getAsBoolean() == true
  //               || opCon.x().getAsBoolean());
  // }
  //
  // public static final Trigger L3() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(6) && eSwitch().getAsBoolean() == false
  //               || opCon.povUp().getAsBoolean());
  // }
  //
  // public static final Trigger zeroManipulator() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(6) && eSwitch().getAsBoolean() == true
  //               || opCon.y().getAsBoolean());
  // }
  //
  // public static final Trigger L4() {
  //   return new Trigger(
  //       () ->
  //           buttonboard.getRawButton(7) && eSwitch().getAsBoolean() == false
  //               || opCon.povRight().getAsBoolean());
  // }

  // public static final DoubleSupplier manualClimber() {
  //   return () -> buttonboard.getBigSwitchY();
  // }
  //
  // public static final DoubleSupplier manualElevator() {
  //   return () -> buttonboard.getBigSwitchY();
  // }
  //
  // public static final DoubleSupplier manualIntake() {
  //   return () -> buttonboard.getBigSwitchX();
  // }
  //
  // public static final DoubleSupplier manualManipulator() {
  //   return () -> buttonboard.getBigSwitchX();
  // }

  public void driverControls(
      Intake Intake,
      Climber Climber,
      Elevator Elevator,
      Manipulator Manipulator,
      CommandSwerveDrivetrain Drivetrain) {

    Drivetrain.registerTelemetry(logger::telemeterize);

    final SwerveRequest.FieldCentric drive =
        new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1)
            .withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(
                DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    Drivetrain.setDefaultCommand(
        // Drivetrain will execute this command periodically
        Drivetrain.applyRequest(
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

    driverCon.back().and(driverCon.y()).whileTrue(Drivetrain.sysIdDynamic(Direction.kForward));
    driverCon.back().and(driverCon.x()).whileTrue(Drivetrain.sysIdDynamic(Direction.kReverse));
    driverCon.start().and(driverCon.y()).whileTrue(Drivetrain.sysIdQuasistatic(Direction.kForward));
    driverCon.start().and(driverCon.x()).whileTrue(Drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on y button press
    driverCon.y().onTrue(Drivetrain.runOnce(() -> Drivetrain.seedFieldCentric()));
    driverCon.a().whileTrue(new runIntakeTrough(Intake, -1.5));

    /* Other Subsystems Declairations */

    // Buttons to make Manip work
    driverCon.rightBumper().whileTrue(new manipIntake(Manipulator, Manipulator.laserPassed()));

    // Trough Shot Button
    driverCon.leftBumper().whileTrue(new runIntakeTrough(Intake, 7));

    // Command to run the intake const and make it work
    Intake.setDefaultCommand(new runIntake(Intake, driverCon.leftTrigger(0.5), 4.0));

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
        .whileTrue(new ReefAlign(Drivetrain, true));

    driverCon
        .y()
        .and(
            () -> {
              var validTags =
                  AllianceFlipUtil.get(
                      FieldConstants.BLUE_VALID_REEF_TAGS, FieldConstants.RED_VALID_REEF_TAGS);
              return validTags.contains((int) lastTagID);
            })
        .whileTrue(new ReefAlign(Drivetrain, false));
  }

  public void opControls(
      Intake Intake,
      Climber Climber,
      Elevator Elevator,
      Manipulator Manipulator,
      CommandSwerveDrivetrain Drivetrain) {

    /* Without eSwitch */

    opCon.leftBumper();

    // Run Climber When Pushed
    opCon
        .rightBumper()
        .whileTrue(new runClimber(Climber, ClimberConstants.setpointForClimb))
        .whileFalse(new runClimber(Climber, 0.0));

    // Go To Elevator setpoints When Pushed
    opCon
        .povUp()
        .onTrue(
            new runElevator(
                Elevator, ElevatorConstants.L3Setpoint, Manipulator, driverCon.rightTrigger(0.5)));

    opCon
        .povLeft()
        .whileTrue(new manipPivot(Manipulator, 5, true))
        .whileFalse(new manipPivot(Manipulator, 0, false));

    opCon.povRight().onTrue(new runElevator(Elevator, 20, Manipulator, driverCon.rightTrigger()));

    opCon
        .povDown()
        .onTrue(new manipPivot(Manipulator, 5.5, true).alongWith(new runIntakeTrough(Intake, 2)))
        .onFalse(
            new manipPivot(Manipulator, 0, false)
                .alongWith(Commands.runOnce(() -> Intake.resetFlywheel())));

    /* With eSwitch */

    // Zero Subsystems When Pushed
    opCon.x().whileTrue(Commands.runOnce(() -> Climber.zeroClimber()));
    opCon.b().whileTrue(Commands.runOnce(() -> Elevator.setElevatorZero()));
    opCon.leftTrigger().whileTrue(Commands.runOnce(() -> Intake.zeroIntakePivot()));
    // opCon.rightBumper().whileTrue(Commands.runOnce(() -> Manipulator.zeroManip()));

    opCon
        .rightTrigger(0.5)
        .whileTrue(new manipPivot(Manipulator, ManipulatorConstants.manipSetpoint, false))
        .whileFalse(new manipPivot(Manipulator, 0, false));
  }
}
