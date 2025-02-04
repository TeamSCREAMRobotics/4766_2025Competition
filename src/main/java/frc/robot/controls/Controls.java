package frc.robot.controls;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsytems.Climber;
import java.util.function.DoubleSupplier;

public class Controls {
  public static CommandXboxController driverCon = new CommandXboxController(0);
  public static CommandXboxController opCon = new CommandXboxController(1);
  public static Buttonboard buttonboard = new Buttonboard(2);
  private Climber s_Climber = new Climber();

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

  public static void driverControls() {}

  public static void opControls() {}

  public static void buttonBoard() {}
}
