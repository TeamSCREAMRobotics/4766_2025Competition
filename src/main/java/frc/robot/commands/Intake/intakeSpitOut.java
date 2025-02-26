// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Intake;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class intakeSpitOut extends Command {
  Intake s_Intake;
  double setpoint;
  private boolean timeUp;
  private Timer timer = new Timer();
  /** Creates a new intakePivot. */
  public intakeSpitOut(Intake intake, double Setpoint) {
    intake = s_Intake;
    setpoint = Setpoint;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_Intake.goToSetpoint(setpoint);
    s_Intake.runFlywheel(-7);
    timer.start();
    if (timer.hasElapsed(2)) {
      timeUp = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(setpoint == 0.0){
      s_Intake.resetPIDMotor();
      s_Intake.resetFlywheel();
    }
    else{
      s_Intake.resetFlywheel();
      s_Intake.goToSetpoint(0.0);
    }

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timeUp;
  }
}
