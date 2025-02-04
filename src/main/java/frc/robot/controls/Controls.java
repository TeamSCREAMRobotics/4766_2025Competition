package frc.robot.controls;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Climber.runClimber;
import frc.robot.commands.Intake.runIntake;
import frc.robot.subsytems.Climber;
import frc.robot.subsytems.Elevator;
import frc.robot.subsytems.Intake;
import frc.robot.subsytems.Manipulator;
import java.util.function.DoubleSupplier;

public class Controls {
  public static CommandXboxController driverCon = new CommandXboxController(0);
  public static CommandXboxController opCon = new CommandXboxController(1);
  public static Buttonboard buttonboard = new Buttonboard(2);
  private static Climber s_Climber = new Climber();
  private static Elevator s_Elevator = new Elevator();
  private static Intake s_Intake = new Intake();
  private static Manipulator s_Manipulator = new Manipulator();

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

  public static void driverControls() {
    driverCon
        .rightTrigger(0.5).whileTrue(new runIntake(s_Intake));
  }

  public static void opControls() {}

  public static void buttonBoard() {
    // Without eSwitch
    b3().whileTrue(new runClimber(s_Climber, 8.0));

    // With eSwitch
    b7().and(eSwitch()).whileTrue(new InstantCommand(() -> s_Climber.zeroClimber()));
  }
}
