#!/usr/bin/python3
from flask import Flask, Blueprint


class QuiteSimpleWs:
    def __init__(self, name: str, address: str, port: int):
        self.__app = Flask(name)
        self.__address = address
        self.__port = port

    def run(self):
        self.__app.after_request(QuiteSimpleWs.__after_req)
        self.__app.run(self.__address, self.__port)

    def add_blueprint(self, blueprint: Blueprint):
        self.__app.register_blueprint(blueprint)

    @staticmethod
    def __after_req(response):
        response.headers.set('Access-Control-Allow-Origin', '*')
        response.headers.set('Content-Type', 'application/json')
        return response
