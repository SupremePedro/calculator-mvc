$(document).ready(function () {
     const calculator = {
         displayValue: document.querySelector('.calculator-screen').value
     };

    function resetCalculator() {
        calculator.displayValue = '0';
    }

    function inputDecimal(dot) {
        if (!calculator.displayValue.includes(dot)) {
            calculator.displayValue += dot;
        }
    }

    function inputDigit(digit) {
        if (calculator.displayValue === '0') {
            calculator.displayValue = digit;
        } else {
            calculator.displayValue += digit;
        }
    }
    function clearLast() {
        if(calculator.displayValue.length>1){
            calculator.displayValue = calculator.displayValue.replace(/.$/,"");
        }else{
            calculator.displayValue = '0';
        }
    }
    function inputOperator(operator) {
        if("(".includes(calculator.displayValue.charAt(calculator.displayValue.length-1))){
            calculator.displayValue += "0" + operator;
        }
        else if(isLastCharOperator()){
            calculator.displayValue = calculator.displayValue.replace(/.$/,operator);
        }else{
            if (calculator.displayValue === '0') {
                calculator.displayValue = "0" + operator;
            } else {
                calculator.displayValue += operator;
            }
        }
    }

    function equalSignProcess(f){
        let inputValue = document.getElementsByTagName("input")[0].value;
        function haveSameLength(str, a, b){
            return (str.match(a) || [] ).length === (str.match(b) || [] ).length;
        };
        function isBalanced(str){
            var arr = [
                [ /\(/gm, /\)/gm ]
            ], i = arr.length, isClean = true;

            while( i-- && isClean ){
                isClean = haveSameLength( str, arr[i][0], arr[i][1] );
            }
            return isClean;
        };
        if(!isBalanced(inputValue)){
            alert("Error: Wrong brackets format!!!");
        }else if(isLastCharOperator()){
            alert("Error: Wrong operator format!!!");
        }else{
            document.getElementsByTagName("input")[0].value = inputValue.replace(/,/,"");
            f.action = "/evaluate";
            f.method = "GET";
            f.submit();


        }
    }

    function updateDisplay() {
        const display = document.querySelector('.calculator-screen');
        display.value = calculator.displayValue;
    }

    updateDisplay();

    const keys = document.querySelector('.calculator-keys');

    function redoProcess(f) {
        console.log("redo");
        f.action = "/redo";
        f.method = "GET";
        f.submit();
    }
    function isLastCharOperator() {
        const operatorCharacter = "/*-+";
        return operatorCharacter.includes(calculator.displayValue.charAt(calculator.displayValue.length-1));
    }
    function undoProcess(f){
        console.log("undo");
        f.action = "/undo";
        f.method = "GET";
        f.submit();
    }

    function inputBracket(value) {
        if(value==")"){
            if(!isLastCharOperator()){
                calculator.displayValue += value;
            }
        }else{
            if (calculator.displayValue === '0') {
                calculator.displayValue = value;
            }else {
                calculator.displayValue += value;
            }
        }
    }

    keys.addEventListener('click', (event) => {
        const {target} = event;
        const f = document.getElementById("calculator-form");
        if (!target.matches('button')) {
            return;
        }

        if (target.classList.contains('operator')) {
            inputOperator(target.value);
            updateDisplay();
            return;
        }
        if (target.classList.contains('redo')) {
            redoProcess(f);
            updateDisplay();
            return;
        }
        if (target.classList.contains('undo')) {
            undoProcess(f);
            updateDisplay();
            return;
        }

        if (target.classList.contains('decimal')) {
            inputDecimal(target.value);
            updateDisplay();
            return;
        }

        if (target.classList.contains('all-clear')) {
            resetCalculator();
            updateDisplay();
            return;
        }
        if (target.classList.contains('clear-last')) {
            clearLast();
            updateDisplay();
            return;
        }
        if (target.classList.contains('equal-sign')) {
            equalSignProcess(f);
            updateDisplay();
            return;
        }
        if (target.classList.contains('digit')) {
            inputDigit(target.value);
            updateDisplay();
        }
        if (target.classList.contains('bracket')) {
            inputBracket(target.value);
            updateDisplay();
        }
    });
});