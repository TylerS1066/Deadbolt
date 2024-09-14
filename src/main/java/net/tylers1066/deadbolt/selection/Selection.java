package net.tylers1066.deadbolt.selection;

import net.tylers1066.deadbolt.db.Deadbolt;
import net.tylers1066.deadbolt.util.EnhancedSign;

public class Selection {
    private final EnhancedSign sign;
    private final Deadbolt deadbolt;

    public Selection(EnhancedSign sign, Deadbolt deadbolt) {
        this.sign = sign;
        this.deadbolt = deadbolt;
    }

    public EnhancedSign getSign() {
        return sign;
    }

    public Deadbolt getDeadbolt() {
        return deadbolt;
    }
}
