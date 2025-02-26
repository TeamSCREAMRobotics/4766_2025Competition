// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsytems.Intake;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class intakeIn extends Command {
  Intake s_Intake;
  double setpoint;
  private boolean algaeIn;
  /** Creates a new intakePivot. */
  public intakeIn(Intake intake, double Setpoint) {
    intake = s_Intake;
    setpoint = Setpoint;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(s_Intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    algaeIn = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    s_Intake.goToSetpoint(setpoint);
    s_Intake.runFlywheel(7);
    if(s_Intake.amperageSpiked(0.5)){
      algaeIn = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(algaeIn){
      s_Intake.runFlywheel(2);
      s_Intake.goToSetpoint(0.5);
    }
    else if(!algaeIn){
    s_Intake.resetFlywheel();
    s_Intake.goToSetpoint(0.0);
    s_Intake.resetPIDMotor();

    } 
    

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return algaeIn;
  }
}
