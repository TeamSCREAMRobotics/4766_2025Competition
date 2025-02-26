package frc.robot.controls;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.Climber.manualClimb;
import frc.robot.commands.Climber.runClimber;
import frc.robot.commands.Elevator.manualElevator;
import frc.robot.commands.Intake.intakeIn;
import frc.robot.commands.Intake.intakeSpitOut;
import frc.robot.commands.Manipulator.manipIntake;
import frc.robot.commands.Manipulator.manipOuttake;
import frc.robot.constants.Constants.ClimberConstants;
import frc.robot.constants.TunerConstants;
import frc.robot.subsytems.Climber;
import frc.robot.subsytems.CommandSwerveDrivetrain;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Intake;
import frc.robot.subsytems.Manipulator;
import java.util.function.DoubleSupplier;

public class Controls {
  public static CommandXboxController driverCon = new CommandXboxController(2);
  public static CommandXboxController opCon = new CommandXboxController(0);
  public static Buttonboard buttonboard = new Buttonboard(1);
  private static Climber s_Climber;
  private static Elevator s_Elevator;
  private static Intake s_Intake;
  private static Manipulator s_Manipulator;
  private static CommandSwerveDrivetrain s_Drivetrain;
  boolean eSwitchA = eSwitch().getAsBoolean() == true;

  private static double MaxSpeed =
      TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
  private static double MaxAngularRate =
      RotationsPerSecond.of(0.75)
          .in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */

  public static final Trigger eSwitch() {
    return new Trigger(() -> buttonboard.getRawSwitch(8));
  }

  public static final Trigger b1() {
    return new Trigger(() -> buttonboard.getRawButton(1));
  }

  public static final Trigger b2() {
    return new Trigger(() -> buttonboard.getRawButton(2));
  }

  public static final Trigger b3() {
    return new Trigger(() -> buttonboard.getRawButton(3));
  }

  public static final Trigger b4() {
    return new Trigger(() -> buttonboard.getRawButton(4));
  }

  public static final Trigger b5() {
    return new Trigger(() -> buttonboard.getRawButton(5));
  }

  public static final Trigger b6() {
    return new Trigger(() -> buttonboard.getRawButton(6));
  }

  public static final Trigger b7() {
    return new Trigger(() -> buttonboard.getRawButton(7));
  }

  public static final DoubleSupplier JoyY() {
    return () -> buttonboard.getBigSwitchY();
  }

  public static final DoubleSupplier JoyX() {
    return () -> buttonboard.getBigSwitchX();
  }

  public static final Trigger runClimber() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(3) && eSwitch().getAsBoolean() == false
                || opCon.leftBumper().getAsBoolean());
  }

  public static final Trigger resetClimber() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(3) && eSwitch().getAsBoolean() == true
                || opCon.y().getAsBoolean());
  }

  public static final DoubleSupplier manualClimber() {
    return () -> buttonboard.getBigSwitchY();
  }

  public static final DoubleSupplier manualElevator() {
    return () -> buttonboard.getBigSwitchY();
  }

  public static final Trigger L1() {
    return new Trigger(() -> buttonboard.getRawButton(4) || opCon.povDown().getAsBoolean());
  }

  public static final Trigger L2() {
    return new Trigger(() -> buttonboard.getRawButton(5) || opCon.povLeft().getAsBoolean());
  }

  public static final Trigger L3() {
    return new Trigger(() -> buttonboard.getRawButton(6) || opCon.povUp().getAsBoolean());
  }

  public static final Trigger L4() {
    return new Trigger(() -> buttonboard.getRawButton(7) || opCon.povDownRight().getAsBoolean());
  }

  public static void driverControls(
      Intake Intake,
      Climber Climber,
      Elevator Elevator,
      Manipulator Manipulator,
      CommandSwerveDrivetrain Drivetrain) {
    s_Intake = Intake;
    s_Climber = Climber;
    s_Elevator = Elevator;
    s_Manipulator = Manipulator;
    s_Drivetrain = Drivetrain;

    final SwerveRequest.FieldCentric drive =
        new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1)
            .withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(
                DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    driverCon.leftTrigger(0.5).whileTrue(new intakeSpitOut(s_Intake, 0));
    driverCon.leftBumper().onTrue(new intakeIn(s_Intake, 0));

    driverCon.rightBumper().onTrue(new manipIntake(Manipulator));
    driverCon.rightTrigger(0.5).onTrue(new manipOuttake(Manipulator));

    s_Drivetrain.setDefaultCommand(
        // Drivetrain will execute this command periodically
        s_Drivetrain.applyRequest(
            () ->
                drive
                    .withVelocityX(
                        -driverCon.getLeftY() * 0.8) // Drive forward with negative Y (forward)
                    .withVelocityY(-driverCon.getLeftX() * 0.8) // Drive left with negative X (left)
                    .withRotationalRate(
                        -driverCon.getRightX()
                            * 0.9) // Drive counterclockwise with negative X (left)
            ));

    driverCon.back().and(driverCon.y()).whileTrue(s_Drivetrain.sysIdDynamic(Direction.kForward));
    driverCon.back().and(driverCon.x()).whileTrue(s_Drivetrain.sysIdDynamic(Direction.kReverse));
    driverCon
        .start()
        .and(driverCon.y())
        .whileTrue(s_Drivetrain.sysIdQuasistatic(Direction.kForward));
    driverCon
        .start()
        .and(driverCon.x())
        .whileTrue(s_Drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on left bumper press
    driverCon.leftBumper().onTrue(s_Drivetrain.runOnce(() -> s_Drivetrain.seedFieldCentric()));
  }

  public static void opControls(
      Intake Intake,
      Climber Climber,
      Elevator Elevator,
      Manipulator Manipulator,
      CommandSwerveDrivetrain Drivetrain) {
    s_Intake = Intake;
    s_Climber = Climber;
    s_Elevator = Elevator;
    s_Manipulator = Manipulator;
    s_Drivetrain = Drivetrain;
    // Without eSwitch
    runClimber()
        .toggleOnTrue(new runClimber(s_Climber, ClimberConstants.setpointForClimb))
        .toggleOnFalse(new runClimber(s_Climber, 0.0));
    s_Elevator.setDefaultCommand(new manualElevator(s_Elevator));

    // With eSwitch
    resetClimber().whileTrue(new InstantCommand(() -> s_Climber.zeroClimber()));
    s_Climber.setDefaultCommand(new manualClimb(s_Climber, manualClimber()));
  }
}
