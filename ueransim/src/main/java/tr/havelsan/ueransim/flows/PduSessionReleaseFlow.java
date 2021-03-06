package tr.havelsan.ueransim.flows;

import fr.marben.asnsdk.japi.spe.ContainingOctetStringValue;
import tr.havelsan.ueransim.BaseFlow;
import tr.havelsan.ueransim.FlowLogging;
import tr.havelsan.ueransim.IncomingMessage;
import tr.havelsan.ueransim.SendingMessage;
import tr.havelsan.ueransim.configs.PduSessionReleaseConfig;
import tr.havelsan.ueransim.contexts.SimulationContext;
import tr.havelsan.ueransim.nas.NasEncoder;
import tr.havelsan.ueransim.nas.impl.enums.EPduSessionIdentity;
import tr.havelsan.ueransim.nas.impl.enums.EProcedureTransactionIdentity;
import tr.havelsan.ueransim.nas.impl.ies.IEPayloadContainer;
import tr.havelsan.ueransim.nas.impl.ies.IEPayloadContainerType;
import tr.havelsan.ueransim.nas.impl.ies.IEPduSessionIdentity2;
import tr.havelsan.ueransim.nas.impl.messages.PduSessionReleaseComplete;
import tr.havelsan.ueransim.nas.impl.messages.PduSessionReleaseRequest;
import tr.havelsan.ueransim.nas.impl.messages.UlNasTransport;
import tr.havelsan.ueransim.ngap.ngap_ies.PDUSessionID;
import tr.havelsan.ueransim.ngap.ngap_ies.PDUSessionResourceReleaseResponseTransfer;
import tr.havelsan.ueransim.ngap.ngap_ies.PDUSessionResourceReleasedItemRelRes;
import tr.havelsan.ueransim.ngap.ngap_ies.PDUSessionResourceReleasedListRelRes;
import tr.havelsan.ueransim.ngap.ngap_pdu_contents.PDUSessionResourceReleaseCommand;
import tr.havelsan.ueransim.ngap2.NgapBuilder;
import tr.havelsan.ueransim.ngap2.NgapCriticality;
import tr.havelsan.ueransim.ngap2.NgapProcedure;
import tr.havelsan.ueransim.utils.octets.OctetString;

import java.util.ArrayList;

public class PduSessionReleaseFlow extends BaseFlow {
    private PduSessionReleaseConfig config;

    public PduSessionReleaseFlow(SimulationContext simContext, PduSessionReleaseConfig config) {
        super(simContext);
        this.config = config;
    }

    @Override
    public State main(IncomingMessage message) {
        var pduRR = new PduSessionReleaseRequest();
        pduRR.pduSessionId = EPduSessionIdentity.fromValue(config.pduSessionId.intValue());
        pduRR.pti = EProcedureTransactionIdentity.fromValue(config.procedureTransactionId.intValue());

        var uplink = new UlNasTransport();
        uplink.payloadContainerType = new IEPayloadContainerType(IEPayloadContainerType.EPayloadContainerType.N1_SM_INFORMATION);
        uplink.payloadContainer = new IEPayloadContainer(new OctetString(NasEncoder.nasPdu(pduRR)));
        uplink.pduSessionId = new IEPduSessionIdentity2(config.pduSessionId);
        uplink.sNssa = config.sNssai;
        uplink.dnn = config.dnn;

        send(new SendingMessage(new NgapBuilder(NgapProcedure.UplinkNASTransport, NgapCriticality.IGNORE), uplink));
        return this::waitPduSessionReleaseCommand;
    }

    private State waitPduSessionReleaseCommand(IncomingMessage message) {
        var pduSessionResourceReleaseCommand = message.getNgapMessage(PDUSessionResourceReleaseCommand.class);
        if (pduSessionResourceReleaseCommand == null) {
            FlowLogging.logUnhandledMessage(message, PDUSessionResourceReleaseCommand.class);
            return this::waitPduSessionReleaseCommand;
        }

        sendPDUSessionResourceReleased();
        sendUplinkNas();
        return flowComplete();
    }

    private void sendPDUSessionResourceReleased() {
        var list = new PDUSessionResourceReleasedListRelRes();
        list.valueList = new ArrayList<>();

        var item = new PDUSessionResourceReleasedItemRelRes();
        item.pDUSessionID = new PDUSessionID(config.pduSessionId.intValue());
        item.pDUSessionResourceReleaseResponseTransfer = new ContainingOctetStringValue(new PDUSessionResourceReleaseResponseTransfer());

        send(new SendingMessage(new NgapBuilder(NgapProcedure.PDUSessionResourceReleaseResponse, NgapCriticality.REJECT)
                .addProtocolIE(list, NgapCriticality.IGNORE), null));
    }

    private void sendUplinkNas() {
        var releaseComplete = new PduSessionReleaseComplete();
        releaseComplete.pduSessionId = EPduSessionIdentity.fromValue(config.pduSessionId.intValue());
        releaseComplete.pti = EProcedureTransactionIdentity.fromValue(config.procedureTransactionId.intValue());

        var uplink = new UlNasTransport();
        uplink.payloadContainerType = new IEPayloadContainerType(IEPayloadContainerType.EPayloadContainerType.N1_SM_INFORMATION);
        uplink.payloadContainer = new IEPayloadContainer(new OctetString(NasEncoder.nasPdu(releaseComplete)));
        uplink.pduSessionId = new IEPduSessionIdentity2(config.pduSessionId);
        uplink.sNssa = config.sNssai;
        uplink.dnn = config.dnn;

        send(new SendingMessage(new NgapBuilder(NgapProcedure.UplinkNASTransport, NgapCriticality.IGNORE), uplink));
    }
}
