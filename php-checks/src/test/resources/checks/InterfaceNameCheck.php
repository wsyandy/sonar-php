<?php
interface MyFirstInterface {} // OK
interface mySecondInterface {} // NOK {{Rename this interface name to match the regular expression ^[A-Z][a-zA-Z0-9]*$.}}
