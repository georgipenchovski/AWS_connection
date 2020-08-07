package com.example.aws

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobileconnectors.iot.AWSIotKeystoreHelper
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    companion object {
        const val ENDPOINT = "a9srgrfy3r7is-ats.iot.eu-central-1.amazonaws.com"
        const val CLIENT_ID = "phone"
        const val KEYSTORE_NAME = "keystore"
        const val KEYSTORE_PASSWORD = ""
        const val CERTIFICATE_ID = ""
        const val CERT_PEM = """-----BEGIN CERTIFICATE-----
MIIDWjCCAkKgAwIBAgIVAIaIIa8nI5cwZANPXt8dQuPK4M0rMA0GCSqGSIb3DQEB
CwUAME0xSzBJBgNVBAsMQkFtYXpvbiBXZWIgU2VydmljZXMgTz1BbWF6b24uY29t
IEluYy4gTD1TZWF0dGxlIFNUPVdhc2hpbmd0b24gQz1VUzAeFw0yMDA4MDYxNDEw
NTBaFw00OTEyMzEyMzU5NTlaMB4xHDAaBgNVBAMME0FXUyBJb1QgQ2VydGlmaWNh
dGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDWwZG+SV+31BMG4MKZ
kJl8FYYC3brENBBqNT3R7LYrWwqABWVCkyPyeZxMQjyAmSoyGNEaymZhZC0Zd9Am
HUB2EdMWibEcNccao+cU2qsldEMstpdMSlOFHHXBdz4RUtXvSD2LOWvuZTPQwsc5
x+3vgbCc/LvxL08w2zyL3Q4ay0wvZ5v2SOBRAMGI+h+FAVTfBu780rZkS9404hEV
whZKLguQazlDrcljMdHeLvPRQ6Hfnia+ZzHQrSDAz9QEcyMICSdiy37ooQv4nZEQ
uLeirePGXafVB8GHYiNkL6/GvjXGV3WUEWl7wKwT0YXWpQYgoYbVJoOk+HyVMxeM
fZAFAgMBAAGjYDBeMB8GA1UdIwQYMBaAFNjQUmgSUzz4VT7MLGrgsJ/LNg9WMB0G
A1UdDgQWBBRqdQYL7sOXRg2ErTXMrKvXUS9wxDAMBgNVHRMBAf8EAjAAMA4GA1Ud
DwEB/wQEAwIHgDANBgkqhkiG9w0BAQsFAAOCAQEAMhucJWASvJlNDP8/BqHCsTi0
Mqo0PTueE/giQym2Gq0kv0yokl5EiI/9niYOcACtmyi+mZClgDwZPa+60GM6Q3u7
1zhHgoh1A8izo1IAMkFbU5Kou8K6U421pj1tkc+S9uaCcJIwYSFmtSzb5FvNdLAs
wmTxVJ3Wr1sjHU9pvlxcb1vwIwpEX3VE4w14v5NZn8c+DPGihHOKoLDXUJN4a5gz
xjllWshXeynz8ip8Rb3dNV9HHxX8DO1Y/xAIKxsBb1ohV6SO8w22DqOlC79mr69u
67hlx4g4G4Hhi6PaP41NtLuP5KKnTrNvCSfhP3ND+u3WWKtw+Sbfpowx1rM3xg==
-----END CERTIFICATE-----
"""
        const val PRIVATE_KEY = """-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEA1sGRvklft9QTBuDCmZCZfBWGAt26xDQQajU90ey2K1sKgAVl
QpMj8nmcTEI8gJkqMhjRGspmYWQtGXfQJh1AdhHTFomxHDXHGqPnFNqrJXRDLLaX
TEpThRx1wXc+EVLV70g9izlr7mUz0MLHOcft74GwnPy78S9PMNs8i90OGstML2eb
9kjgUQDBiPofhQFU3wbu/NK2ZEveNOIRFcIWSi4LkGs5Q63JYzHR3i7z0UOh354m
vmcx0K0gwM/UBHMjCAknYst+6KEL+J2RELi3oq3jxl2n1QfBh2IjZC+vxr41xld1
lBFpe8CsE9GF1qUGIKGG1SaDpPh8lTMXjH2QBQIDAQABAoIBAHrDvOTiXO1hNl9A
LP5MQvFD/I+24QU4HgoY1XYjsx+ls6Lrcq9UHcCG9GpDkwKQjCaHOUHvYgdalM1y
SHMBVdAo8Fp5690MgP0hpP+u4RdQFvaAioz6EDSPyGEIIVsV12YqDcSA1so44B3+
upS1TbcUlDmp4GZHfSWYrkvl2iJF+LxXFvMSC1zcyE/1MG+cWEhTF9aIl1E5r1sm
y3jaDH462JF3cMIlJqzbIYMP65TUnhhExwuX3zzYOD1A9m598uZH0M5celNfaIUT
rMXLzHe91qmIoVdG/sQEi4nhmd1uCc/ZWtWXs3K/y0yen0ttb3PdK/NbOnH19/hV
Se+xOSECgYEA9MmtRj6ANRRaPuXunOiO1AI3HxSlZk+bq1qeU8+UVRxNcDmoawiL
Bba4ea/L+sEzh2TyTx0hbvomfQatOof5CTpL3pKRIB+daPgaf/vObHQUPTdI5o1U
hOTk2IQ1T0PYmVEWh4/WJE3fVRAKkwwkXJxViSena1Rktvi61P//vrMCgYEA4Je/
j24kaKlTOApTZJ8X3kEntrHkE0evQ+4Q9tbzY8Dg1WtQs+0IWuuZxb5r1olBclky
Ny7rRRMLcVesfwgeQLJjT4y7+mt4GlyKYRo2wXRkhr9pFa9KILwM5xDeasuMZthl
UZ/1BH4pgxmTXy3oSfO6lMU6HyrM4m6DuWmz0mcCgYEA06CkWC1ixAEDgIti/m6k
RLBhnVJJKKm8iKlAZm7465gbbuG6NEbfgiIVXMbaT8DGXtCsGC+0HvDPY5npG8hA
SnUG8LbAFwieGwkcR/eX3/V/Jels8j3Pi1Ekc/fg6tVaJjqPgsWcuihE45KJwdVm
flZ+MKSCvmzo4bMqO0nTfbcCgYEAhcnf0xFJnw8/A6MjCk27aipl/e76boHFZjHq
6SAi3MmJJZlH+ea5k2/FqoZEtwgMdJGt2DGa9QrjRiF0IDg1zVR21GyBjit24D1A
MZfsBlHPkHV1o/VHszhSd1MVK4kVmi5OLNGco5CEKhSSNaCjP2pxOy5B8U8NvdUX
otvUwwECgYAtNBuLOuBWFsy3LXxiLSDvjEV47UnDjh9EAIDeaCF6EiOvltaJBkal
umqCiRtvGpIdjaKmey9PkszefrVrkQOqDQgJqXI+qqHVcL69/Badi92vyH6CTTLA
ZLU/16vs8bPsSY2o5pNijwxxIJxISAbZotefXdW2R86aDLyZSwFmeA==
-----END RSA PRIVATE KEY-----"""
    }

    private val keystoreName = KEYSTORE_NAME
    private val privateKey = PRIVATE_KEY
    private val certPem = CERT_PEM
    private val keystorePassword = KEYSTORE_PASSWORD
    private val certificateId = CERTIFICATE_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        //run this only first time, then check if exist, or "comment" it
        val saveCertificate = AWSIotKeystoreHelper.saveCertificateAndPrivateKey(certificateId, certPem, privateKey, cacheDir.absolutePath, keystoreName, keystorePassword)

        val path: File = filesDir
        val letDirectory = File(path, "TEST")
        val file = File(letDirectory, "Keystore3.txt")
        letDirectory.mkdirs()
        FileOutputStream(file).use {
            it.write(privateKey.toByteArray())
        }
        connecting()
    }

    private fun connecting() {
        val mqttManager = AWSIotMqttManager(CLIENT_ID, ENDPOINT)
        val clientKeyStore = AWSIotKeystoreHelper.getIotKeystore(certificateId, cacheDir.absolutePath, keystoreName, keystorePassword)
        mqttManager.connect(
                clientKeyStore
        ) { status, throwable ->
            when (status) {
                AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting -> {
                    Timber.d("Connecting...")
                }
                AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected -> {
                    Timber.d("Connected")
                }
                AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Reconnecting -> {
                    if (throwable != null) {
                        Timber.e("Connecting error.")
                    }
                    Timber.d("Reconnecting")
                }
                AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost -> {
                    if (throwable != null) {
                        Timber.e("Connection error.")
                        throwable.printStackTrace()
                    }
                    Timber.d("Disconnected")
                }
                else -> {
                    Timber.d("Disconnected")
                }
            }
        }
    }

}

