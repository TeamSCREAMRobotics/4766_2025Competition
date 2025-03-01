package frc.robot.constants;

public class Constants {
  // Constants for the Climber Subsystem
  public static final class ClimberConstants {
    // TODO: Configure motorIDs for Climber
    public static final int climberMasterID = 15;

    // TODO: Configure SoftLimits for Climber
    public static final double climberForwardSoftLimit = 60;
    public static final double climberReverseSoftLimit = -10;

    // TODO: Configure PID for Climber
    public static final double kG = 0.0;
    public static final double kV = 0.0;
    public static final double kP = 1.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // TODO: Configure MAGIC for Climber
    public static final double kMagicVelocity = 80.0;
    public static final double kMagicAcceleration = 80.0;

    // TODO: Configure setpoint
    public static final double setpointForClimb = 50;
  }

  // Constants for the Elevator Subsystem
  public static final class ElevatorConstants {
    // TODO: Configure motorIDs for Elevator
    public static final int elevatorMasterID = 13;
    public static final int elevatorFollowerID = 14;

    // TODO: Configure SoftLimits for Elevator
    public static final double elevatorForwardSoftLimit = 50;
    public static final double elevatorReverseSoftLimit = -50;

    // TODO: Configure PID for Elevator
    public static final double kG = 0.5;
    public static final double kV = 0.0;
    public static final double kP = 7;
    public static final double kI = 0.0;
    public static final double kD = 0.04;

    // TODO: Configure MAGIC for Elevator
    public static final double kMagicVelocity = 280.0;
    public static final double kMagicAcceleration = 112.0;

    // TODO: Configure setpoints For Elevator
    public static final double L2Setpoint = 5.7;
    public static final double L3Setpoint = 19;
  }

  // Constants for the Intake Subsystem
  public static final class IntakeConstants {
    // TODO: Configure Intake Motor and BeamBreakIDs
    public static final int intakePIDMotorID = 12;
    public static final int intakeMotorID = 11;

    // TODO: Configure Intake SoftLimits
    public static final double intakeFowardSoftLimit = 0.0;
    public static final double intakeReverseSoftLimit = 0.0;

    // TODO: Configure Intake PID (I hate pid)
    public static final double intakeKP = 8;
    public static final double intakeKD = 0;
    public static final double intakeKI = 0;
    public static final double intakeKV = 0.1;
    public static final double intakeKG = 0.5;

    // TODO: Configure Intake MAGIC
    public static final double motionMagicAcceleration = 10;
    public static final double motionMagicCruiseVelocity = 15;

    // TODO: Configure Intake Setpoint
    public static final double intakeSetpoint = 1.26;

    // TODO: Configure AMPS for Intake
    public static final double kAmps = 2.0;
  }

  // Constants for the Manipulator Subsystem
  public static final class ManipulatorConstants {
    // TODO: Configure MotorIDs for Manipulator
    public static final int feederMotorID = 9;
    public static final int pivotMotorID = 10;
    public static final int canRangeID = 0;

    // TODO: Configure SoftLimits for Manipulator
    public static final double climberForwardSoftLimit = 7;
    public static final double climberReverseSoftLimit = 0;

    // TODO: Configure PID for Manipulator
    public static final double kG = 0.5;
    public static final double kV = 0.0;
    public static final double kP = 7;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // TODO: Configure MAGIC for Manipulator
    public static final double kMagicVelocity = 112;
    public static final double kMagicAcceleration = 28;

    // TODO: Configure Laser Threshold Distance for Manipulator
    public static final double kCanRangeDistance = 0.074;

    // TODO: Configure Setpoint For Manip
    public static final double manipSetpoint = 5.6;
  }
}
