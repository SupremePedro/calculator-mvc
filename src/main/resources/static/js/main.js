$(document).ready(function () {
     const calculator = {
         displayValue: document.querySelector('.calculator-screen').value
     };

    function resetCalculator() {
        calculator.displayValue = '0';
    }

    function inputDecimal(dot) {
        // If the `displayValue` does not contain a decimal point
        if (!calculator.displayValue.includes(dot)) {
            // Append the decimal point
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

    function inputOperator(operator) {
        console.log("handleOperator");
        console.log(operator);
        if (calculator.displayValue === '0') {
            calculator.displayValue = "0" + operator;
        } else {
            calculator.displayValue += operator;
        }
    }

    function updateDisplay() {
        const display = document.querySelector('.calculator-screen');
        display.value = calculator.displayValue;
    }

    updateDisplay();

    const keys = document.querySelector('.calculator-keys');
    keys.addEventListener('click', (event) => {
        const {target} = event;
        if (!target.matches('button')) {
            return;
        }

        if (target.classList.contains('operator')) {
            inputOperator(target.value);
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
        if (target.classList.contains('equal-sign')) {
            updateDisplay();
            return;
        }
        if (target.classList.contains('digit')) {
            inputDigit(target.value);
            updateDisplay();
        }
    });
});