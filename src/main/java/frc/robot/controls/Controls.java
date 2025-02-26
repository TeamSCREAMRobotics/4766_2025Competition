package frc.robot.controls;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.Climber.manualClimb;
import frc.robot.commands.Climber.runClimber;
import frc.robot.commands.Elevator.manualElevator;
import frc.robot.commands.Elevator.runElevator;
import frc.robot.commands.Intake.intakeIn;
import frc.robot.commands.Intake.intakeSpitOut;
import frc.robot.commands.Intake.manualIntake;
import frc.robot.commands.Manipulator.manipIntake;
import frc.robot.commands.Manipulator.manipOuttake;
import frc.robot.commands.Manipulator.manipPivot;
import frc.robot.commands.Manipulator.manualMinip;
import frc.robot.constants.Constants.ClimberConstants;
import frc.robot.constants.Constants.ElevatorConstants;
import frc.robot.constants.Constants.ManipulatorConstants;
import frc.robot.subsytems.Climber;
import frc.robot.subsytems.CommandSwerveDrivetrain;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Intake;
import frc.robot.subsytems.Manipulator;
import java.util.function.DoubleSupplier;

public class Controls {
  public static Buttonboard buttonboard = new Buttonboard(0);
  public static CommandXboxController driverCon = new CommandXboxController(1);
  public static CommandXboxController opCon = new CommandXboxController(2);

  /* Setting up bindings for necessary control of the swerve drive platform */

  public static final Trigger eSwitch() {
    return new Trigger(() -> buttonboard.getRawSwitch(8));
  }

  public static final Trigger goToLeft() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(1) && eSwitch().getAsBoolean() == false
                || opCon.leftTrigger(0.5).getAsBoolean());
  }

  public static final Trigger goToRight() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(2) && eSwitch().getAsBoolean() == false
                || opCon.rightTrigger(0.5).getAsBoolean());
  }

  public static final Trigger runClimber() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(3) && eSwitch().getAsBoolean() == false
                || opCon.rightBumper().getAsBoolean());
  }

  public static final Trigger zeroClimber() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(3) && eSwitch().getAsBoolean() == true
                || opCon.a().getAsBoolean());
  }

  public static final Trigger L1() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(4) && eSwitch().getAsBoolean() == false
                || opCon.povDown().getAsBoolean());
  }

  public static final Trigger zeroElevator() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(4) && eSwitch().getAsBoolean() == true
                || opCon.b().getAsBoolean());
  }

  public static final Trigger L2() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(5) && eSwitch().getAsBoolean() == false
                || opCon.povLeft().getAsBoolean());
  }

  public static final Trigger zeroIntake() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(5) && eSwitch().getAsBoolean() == true
                || opCon.x().getAsBoolean());
  }

  public static final Trigger L3() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(6) && eSwitch().getAsBoolean() == false
                || opCon.povUp().getAsBoolean());
  }

  public static final Trigger zeroManipulator() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(6) && eSwitch().getAsBoolean() == true
                || opCon.y().getAsBoolean());
  }

  public static final Trigger L4() {
    return new Trigger(
        () ->
            buttonboard.getRawButton(7) && eSwitch().getAsBoolean() == false
                || opCon.povRight().getAsBoolean());
  }

  public static final DoubleSupplier manualClimber() {
    return () -> buttonboard.getBigSwitchY();
  }

  public static final DoubleSupplier manualElevator() {
    return () -> buttonboard.getBigSwitchY();
  }

  public static final DoubleSupplier manualIntake() {
    return () -> buttonboard.getBigSwitchX();
  }

  public static final DoubleSupplier manualManipulator() {
    return () -> buttonboard.getBigSwitchX();
  }

  public static void driverControls(
      Intake Intake,
      Climber Climber,
      Elevator Elevator,
      Manipulator Manipulator,
      CommandSwerveDrivetrain Drivetrain,
      double vel,
      double rot) {

    final SwerveRequest.FieldCentric drive =
        new SwerveRequest.FieldCentric()
            .withDeadband(vel * 0.1)
            .withRotationalDeadband(rot * 0.1) // Add a 10% deadband
            .withDriveRequestType(
                DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    Drivetrain.setDefaultCommand(
        // Drivetrain will execute this command periodically
        Drivetrain.applyRequest(
            () ->
                drive
                    .withVelocityX(
                        -driverCon.getLeftY() * vel) // Drive forward with negative Y (forward)
                    .withVelocityY(-driverCon.getLeftX() * vel) // Drive left with negative X (left)
                    .withRotationalRate(
                        -driverCon.getRightX()
                            * rot) // Drive counterclockwise with negative X (left)
            ));

    driverCon.back().and(driverCon.y()).whileTrue(Drivetrain.sysIdDynamic(Direction.kForward));
    driverCon.back().and(driverCon.x()).whileTrue(Drivetrain.sysIdDynamic(Direction.kReverse));
    driverCon.start().and(driverCon.y()).whileTrue(Drivetrain.sysIdQuasistatic(Direction.kForward));
    driverCon.start().and(driverCon.x()).whileTrue(Drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on y button press
    driverCon.y().onTrue(Drivetrain.runOnce(() -> Drivetrain.seedFieldCentric()));

    // Other Subsystems Declairations
    driverCon.leftTrigger(0.5).whileTrue(new intakeSpitOut(Intake, 0));
    driverCon.leftBumper().onTrue(new intakeIn(Intake, 0));

    driverCon.rightBumper().whileTrue(new manipIntake(Manipulator));
    driverCon.rightTrigger(0.5).onTrue(new manipOuttake(Manipulator));
  }

  public static void opControls(
      Intake Intake,
      Climber Climber,
      Elevator Elevator,
      Manipulator Manipulator,
      CommandSwerveDrivetrain Drivetrain) {

    /* Without eSwitch */

    // Makes the Robot Move Left When Pushed
    goToLeft().whileTrue(null);

    // Makes the Robot Move Right When Pushed
    goToRight().whileTrue(null);

    // Run Climber When Pushed
    runClimber()
        .toggleOnTrue(new runClimber(Climber, ClimberConstants.setpointForClimb))
        .toggleOnFalse(new runClimber(Climber, 0.0));

    // Go To Elevator setpoints When Pushed
    L1().whileTrue(new runElevator(Elevator, 0));
    L2().whileTrue(
            new runElevator(Elevator, ElevatorConstants.L2Setpoint)
                .alongWith(new manipPivot(Manipulator, ManipulatorConstants.manipSetpoint)));
    L3().whileTrue(
            new runElevator(Elevator, ElevatorConstants.L3Setpoint)
                .alongWith(new manipPivot(Manipulator, ManipulatorConstants.manipSetpoint)));

    // Joysticks Commands
    Elevator.setDefaultCommand(new manualElevator(Elevator, manualElevator()));
    Intake.setDefaultCommand(new manualIntake(Intake, manualIntake()));

    /* With eSwitch */

    // Zero Subsystems When Pushed
    zeroClimber().whileTrue(new InstantCommand(() -> Climber.zeroClimber()));
    zeroElevator().whileTrue(new InstantCommand(() -> Elevator.setElevatorZero()));
    zeroIntake().whileTrue(new InstantCommand(() -> Intake.zeroIntakePivot()));
    zeroManipulator().whileTrue(new InstantCommand(() -> Intake.zeroIntakePivot()));

    // Joysticks Commands
    Climber.setDefaultCommand(new manualClimb(Climber, manualClimber()));
    Manipulator.setDefaultCommand(new manualMinip(Manipulator, manualManipulator()));
  }
}
