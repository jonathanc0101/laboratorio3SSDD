import json
import requests
from time import sleep

hdr = {"Content-Type": "application/json"}
puertos = ["8092", "8091"]
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

    saldoFinal = {"8092": getSaldo("8092")+1300,
                  "8091": getSaldo("8091")+1300
                  }

    depositar("8091", "100")
    depositar("8091", "300")
    depositar("8091", "100")
    depositar("8091", "100")

    depositar("8092", "100")
    depositar("8092", "500")
    depositar("8092", "100")




    print("SALDO ESPERADO [8091]: " + str(saldoFinal["8091"]))
    print("SALDO ESPERADO [8092]: " + str(saldoFinal["8092"]))

    sleep(2)
    imprimirSaldos()

test()
