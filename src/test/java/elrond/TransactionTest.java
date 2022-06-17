package elrond;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class TransactionTest {
    @Test
    public void shouldSerialize() throws Exception {
        Transaction transaction = new Transaction();

        // Without data field
        transaction.setNonce(0);
        transaction.setValue(new BigInteger("42"));
        transaction.setSender(Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz"));
        transaction.setReceiver(Address.fromBech32("erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(50000);
        transaction.setChainID("1");

        String expected = "{'nonce':0,'value':'42','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':50000,'chainID':'1','version':1}".replace('\'', '"');
        assertEquals(expected, transaction.serialize());

        // With data field
        transaction.setData("foobar");
        expected = "{'nonce':0,'value':'42','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':50000,'data':'Zm9vYmFy','chainID':'1','version':1}".replace('\'', '"');
        assertEquals(expected, transaction.serialize());
    }

    @Test
    public void shouldSign() throws Exception {
        String alicePrivateKey = "1a927e2af5306a9bb2ea777f73e06ecc0ac9aaa72fb4ea3fecf659451394cccf";
        Wallet wallet = new Wallet(alicePrivateKey);

        // With data field
        Transaction transaction = new Transaction();
        transaction.setNonce(7);
        transaction.setValue(new BigInteger("10000000000000000000"));
        transaction.setSender(Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz"));
        transaction.setReceiver(Address.fromBech32("erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(70000);
        transaction.setData("for the book with stake");
        transaction.setChainID("1");
        transaction.sign(wallet);

        String expectedSignature = "096c571889352947f285632d79f2b2ee1b81e7acd19ee20510d34002eba0f999b4720f50211b039dd40914284f84c14eb84815bb045c14dbed036f2e87431307";
        String expectedJson = "{'nonce':7,'value':'10000000000000000000','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':70000,'data':'Zm9yIHRoZSBib29rIHdpdGggc3Rha2U=','chainID':'1','version':1,'signature':'096c571889352947f285632d79f2b2ee1b81e7acd19ee20510d34002eba0f999b4720f50211b039dd40914284f84c14eb84815bb045c14dbed036f2e87431307'}".replace('\'', '"');
        assertEquals(expectedSignature, transaction.getSignature());
        assertEquals(expectedJson, transaction.serialize());

        // Without data field
        transaction = new Transaction();
        transaction.setNonce(8);
        transaction.setValue(new BigInteger("10000000000000000000"));
        transaction.setSender(Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz"));
        transaction.setReceiver(Address.fromBech32("erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(50000);
        transaction.setData("");
        transaction.setChainID("1");
        transaction.sign(wallet);

        expectedSignature = "4a6d8186eae110894e7417af82c9bf9592696c0600faf110972e0e5310d8485efc656b867a2336acec2b4c1e5f76c9cc70ba1803c6a46455ed7f1e2989a90105";
        expectedJson = "{'nonce':8,'value':'10000000000000000000','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':50000,'chainID':'1','version':1,'signature':'4a6d8186eae110894e7417af82c9bf9592696c0600faf110972e0e5310d8485efc656b867a2336acec2b4c1e5f76c9cc70ba1803c6a46455ed7f1e2989a90105'}".replace('\'', '"');
        assertEquals(expectedSignature, transaction.getSignature());
        assertEquals(expectedJson, transaction.serialize());
    }

    @Test
    public void shouldComputeHash() throws Exception {
        Wallet wallet = Wallet.deriveFromMnemonic("blind wisdom book round sing capable taste refuse simple thunder profit goddess bird adult skirt road box patient cost tape lawn invite visual rabbit", 0);

        // Without data field
        Transaction transaction = new Transaction();
        transaction.setNonce(1);
        transaction.setValue(new BigInteger("11500313000000000000"));
        transaction.setReceiver(Address.fromBech32("erd1myj45dn3h0y3l49mjz27c4vssgzqxsc3q2jskerza2089c2k4tcsvu9u8d"));
        transaction.setSender(Address.fromBech32("erd1lta2vgd0tkeqqadkvgef73y0efs6n3xe5ss589ufhvmt6tcur8kq34qkwr"));
        transaction.setGasPrice(1000000000);
        transaction.setGasLimit(60000);
        transaction.setChainID("1");
        transaction.setData("");

        transaction.sign(wallet);

        System.out.println(transaction.serialize());
        assertEquals("5527ad25b1dbddbd6e971c11370a72b6f0e53edf053d3fc31a65c86b217e5499", transaction.computeHash());

        // With data field
        transaction.setData("test data");

        transaction.sign(wallet);

        assertEquals("d3289e845264f5c93ade8288cfd8aa56a7bd28b9b4b5ae773aca44d2825bef41", transaction.computeHash());
    }
}
