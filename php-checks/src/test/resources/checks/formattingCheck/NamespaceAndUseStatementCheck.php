<?php

use my\space\AnotherClass;  // NOK {{Move the use declarations after the namespace declarations.}}
use my\space\MyClass;       // NOK {{Add a blank line after this "use" declaration.}}
namespace another\bar;      // NOK {{Add a blank line after this "namespace another\bar" declaration.}}
{
}

namespace {}      // NOK {{Add a blank line after this "namespace" declaration.}}
{
}


use my\space\MyClass;       // NOK - no blank line after use
{
}

use my\space\MyClass;       // NOK - no blank line after use
// use
{
}

namespace another\bar;      // NOK - no blank line after namespace
// namespace
{
}

namespace                   // OK
{
  use x;                    // OK
}

namespace foo\bar;          // OK

use my\space\MyClass;       // OK

{
}
