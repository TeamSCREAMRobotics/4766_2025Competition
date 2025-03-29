package frc.robot.subsytems.manipulator;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants.ManipulatorConstants;

public class ManipulatorFeeder extends SubsystemBase {
  TalonFX manipFeeder = new TalonFX(ManipulatorConstants.feederMotorID);
  VoltageOut m_request = new VoltageOut(0);

  public ManipulatorFeeder() {}

  public void feed(double voltage) {
    manipFeeder.setControl(m_request.withOutput(voltage));
  }

  public void idleFeed() {
    manipFeeder.setControl(m_request.withOutput(0.7));
  }

  public void stopFeed() {
    manipFeeder.setControl(m_request.withOutput(0));
  }
}
