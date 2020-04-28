#!/usr/bin/python3
import configparser
import io


class Config:
    def __init__(self, conf_file):
        with open(conf_file) as conf:
            self.conf = configparser.RawConfigParser(allow_no_value=True)
            self.conf.read_file(conf)

    def get(self, section, variable):
        try:
            return self.conf.get(section, variable)
        except (ValueError, configparser.Error) as err:
            return ''

    def getint(self, section, variable):
        try:
            return self.conf.getint(section, variable)
        except (ValueError, configparser.Error) as err:
            return -1
