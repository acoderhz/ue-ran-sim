package tr.havelsan.ueransim.nas.impl.ies;

import tr.havelsan.ueransim.nas.core.ies.InformationElement4;
import tr.havelsan.ueransim.utils.OctetInputStream;
import tr.havelsan.ueransim.utils.OctetOutputStream;
import tr.havelsan.ueransim.utils.octets.OctetString;

public class IEAuthenticationFailureParameter extends InformationElement4 {
    public OctetString rawData;

    public IEAuthenticationFailureParameter() {
    }

    public IEAuthenticationFailureParameter(OctetString rawData) {
        this.rawData = rawData;
    }

    @Override
    protected IEAuthenticationFailureParameter decodeIE4(OctetInputStream stream, int length) {
        var res = new IEAuthenticationFailureParameter();
        res.rawData = stream.readOctetString(length);
        return res;
    }

    @Override
    public void encodeIE4(OctetOutputStream stream) {
        stream.writeOctetString(rawData);
    }
}
