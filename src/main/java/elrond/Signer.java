package elrond;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;

public class Signer {
    private final byte[] privateKey;

    public Signer(String hexPrivateKey) {
        this.privateKey = Hex.decode(hexPrivateKey);
    }

    public String sign(String data) {
        return this.sign(data.getBytes(StandardCharsets.UTF_8));
    }

    public String sign(byte[] data) {
        var signer = this.createEd25519Signer();
        signer.update(data, 0, data.length);
        var signature = signer.generateSignature();
        var hex = Hex.encode(signature);
        return new String(hex);
    }

    private Ed25519Signer createEd25519Signer() {
        var parameters = new Ed25519PrivateKeyParameters(this.privateKey, 0);
        var signer = new Ed25519Signer();
        signer.init(true, parameters);
        return signer;
    }
}