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

    contenido =dict(response.json())

    return contenido["saldo"]


def depositar(puerto, monto):
    url = getUrl(puerto, rutas["postDeposito"])
    response = requests.post(url, data=getMontoJSON(monto), headers=hdr)
    print(response.content)


def test():
    imprimirSaldos()

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

    print("SALDO ESPERADO [8081]:600")
    print("SALDO ESPERADO [8082]:700")
    print("SALDO ESPERADO [8083]:2500")
    imprimirSaldos()

test()