import json
import requests

hdr = {"Content-Type": "application/json"}
puertos = ["8081", "8082", "8083"]
rutas = {"getSaldo": "/api/saldo", "postDeposito": "/api/deposito",
         "postExtraccion": "/api/extraccion", "postInteres": "/api/interes"}


def getMontoJSON(monto):
    obj = {"monto": monto}
    text = json.dumps(obj, sort_keys=True, indent=4)
    return text


def getUrl(puerto, direccion):
    urlBasica = "http://localhost:"
    return urlBasica + puerto + direccion


def imprimirSaldos():
    print("SALDOS:")
    for puerto in puertos:
        url = getUrl(puerto, rutas["getSaldo"])
        response = requests.get(url)

        print("Puerto: [" + str(puerto) + "]: " + str(response.content))


def getSaldo(puerto):
    url = getUrl(puerto, rutas["getSaldo"])
    response = requests.get(url)

    contenido = dict(response.json())

    return contenido["saldo"]


def depositar(puerto, monto):
    print("depositando " + monto + " en [" + puerto + "]")

    url = getUrl(puerto, rutas["postDeposito"])
    response = requests.post(url, data=getMontoJSON(monto), headers=hdr)


def test():
    imprimirSaldos()

    saldoFinal = {"8081": getSaldo("8081")+600,
                  "8082": getSaldo("8082")+700, 
                  "8083": getSaldo("8083")+2500}

    depositar("8081", "100")
    depositar("8081", "100")
    depositar("8081", "300")
    depositar("8081", "100")

    depositar("8082", "100")
    depositar("8082", "500")
    depositar("8082", "100")

    depositar("8083", "100")
    depositar("8083", "100")
    depositar("8083", "100")
    depositar("8083", "2200")

    print("SALDO ESPERADO [8081]: " + str(saldoFinal["8081"]))
    print("SALDO ESPERADO [8082]: " + str(saldoFinal["8082"]))
    print("SALDO ESPERADO [8083]: " + str(saldoFinal["8083"]))

    imprimirSaldos()


test()
