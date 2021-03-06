package tr.havelsan.ueransim.nas.impl.ies;

import tr.havelsan.ueransim.nas.core.ies.InformationElement4;
import tr.havelsan.ueransim.utils.OctetInputStream;
import tr.havelsan.ueransim.utils.OctetOutputStream;
import tr.havelsan.ueransim.utils.octets.Octet;

public class IEGprsTimer2 extends InformationElement4 {
    public Octet value;

    public IEGprsTimer2() {
    }

    public IEGprsTimer2(Octet value) {
        this.value = value;
    }

    @Override
    protected IEGprsTimer2 decodeIE4(OctetInputStream stream, int length) {
        var res = new IEGprsTimer2();
        res.value = stream.readOctet();
        return res;
    }

    @Override
    public void encodeIE4(OctetOutputStream stream) {
        stream.writeOctet(value);
    }
}
