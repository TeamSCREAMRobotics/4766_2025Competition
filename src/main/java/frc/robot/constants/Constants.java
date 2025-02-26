package frc.robot.constants;

public class Constants {
  // Constants for the Climber Subsystem
  public static final class ClimberConstants {
    // TODO: Configure motorIDs for Climber
    public static final int climberMasterID = 9;
    public static final int climberFollowerID = 10;

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
    public static final int elevatorMasterID = 0;
    public static final int elevatorFollowerID = 0;

    // TODO: Configure SoftLimits for Elevator
    public static final double elevatorForwardSoftLimit = 0.0;
    public static final double elevatorReverseSoftLimit = 0.0;

    // TODO: Configure PID for Elevator
    public static final double kG = 0.0;
    public static final double kV = 0.0;
    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // TODO: Configure MAGIC for Elevator
    public static final double kMagicVelocity = 0.0;
    public static final double kMagicAcceleration = 0.0;

    // TODO: Configure setpoints For Elevator
    public static final double L2Setpoint = 0.0;
    public static final double L3Setpoint = 0.0;
  }

  // Constants for the Intake Subsystem
  public static final class IntakeConstants {
    // TODO: Configure Intake Motor and BeamBreakIDs
    public static final int intakePIDMotorID = 0;
    public static final int intakeMotorID = 0;

    // TODO: Configure Intake SoftLimits
    public static final double intakeFowardSoftLimit = 0.0;
    public static final double intakeReverseSoftLimit = 0.0;

    // TODO: Configure Intake PID (I hate pid)
    public static final double intakeKP = 0;
    public static final double intakeKD = 0;
    public static final double intakeKI = 0;
    public static final double intakeKV = 0;
    public static final double intakeKG = 0;

    // TODO: Configure Intake MAGIC
    public static final double motionMagicAcceleration = 0;
    public static final double motionMagicCruiseVelocity = 0;
  }

  // Constants for the Manipulator Subsystem
  public static final class ManipulatorConstants {
    // TODO: Configure MotorIDs for Manipulator
    public static final int feederMotorID = 0;
    public static final int pivotMotorID = 0;
    public static final int canRangeID = 0;

    // TODO: Configure SoftLimits for Manipulator
    public static final double climberForwardSoftLimit = 0.0;
    public static final double climberReverseSoftLimit = 0.0;

    // TODO: Configure PID for Manipulator
    public static final double kG = 0.0;
    public static final double kV = 0.0;
    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    // TODO: Configure MAGIC for Manipulator
    public static final double kMagicVelocity = 0.0;
    public static final double kMagicAcceleration = 0.0;

    // TODO: Configure AMPS for Manipulator
    public static final double kAmps = 0.0;

    // TODO: Configure Laser Threshold Distance for Manipulator
    public static final double kCanRangeDistance = 0.075;

    // TODO: Configure Setpoint For Manip
    public static final double manipSetpoint = 0.0;
  }
}
