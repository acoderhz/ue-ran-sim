package tr.havelsan.ueransim.crypto;

import tr.havelsan.ueransim.utils.bits.Bit;
import tr.havelsan.ueransim.utils.bits.Bit5;
import tr.havelsan.ueransim.utils.bits.BitString;
import tr.havelsan.ueransim.utils.octets.Octet4;
import tr.havelsan.ueransim.utils.octets.OctetString;

public class NEA3_128 {

    public static BitString encrypt(Octet4 count, Bit5 bearer, Bit direction, BitString message, OctetString key) {
        return EEA3_128.encrypt(count, bearer, direction, message, key);
    }

    public static BitString decrypt(Octet4 count, Bit5 bearer, Bit direction, BitString message, OctetString key) {
        return EEA3_128.decrypt(count, bearer, direction, message, key);
    }
}
