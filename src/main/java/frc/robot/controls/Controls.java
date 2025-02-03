package frc.robot.controls;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsytems.Climber;

public class Controls {
  public static CommandXboxController driverCon = new CommandXboxController(0);
  public static CommandXboxController opCon = new CommandXboxController(1);
  public static Buttonboard buttonboard = new Buttonboard(2);
  private Climber s_Climber = new Climber();

  public static void driverControls() {}
}
