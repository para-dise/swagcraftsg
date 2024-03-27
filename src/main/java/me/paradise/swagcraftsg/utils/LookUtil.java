package me.paradise.swagcraftsg.utils;

import net.minestom.server.coordinate.Pos;

public class LookUtil {
    public static Pos lookAt(Pos loc, Pos lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.add(0, 0, 0);

        // Values of change in distance (make it relative)
        double dx = lookat.x() - loc.x();
        double dy = lookat.y() - loc.y();
        double dz = lookat.z() - loc.z();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc = loc.withYaw((float) (1.5 * Math.PI));
            } else {
                loc = loc.withYaw((float) (0.5 * Math.PI));
            }
            loc = loc.withYaw((float) loc.yaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc = loc.withYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc = loc.withPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc = loc.withYaw(-loc.yaw() * 180f / (float) Math.PI);
        loc = loc.withPitch(loc.pitch() * 180f / (float) Math.PI);

        return loc;
    }
}
