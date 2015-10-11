	.globl main
main:
	addi $t1,$sp,0
	addi $sp,$sp,-4000
	addi $t2,$sp,0
	addi $t2,$t2,-4000
	la $t3, HEAPPTR
	sw $t2,0($t3)
	sw $ra,4($sp)
	jal main1
	li $v0, 10
	syscall
main1:
	sw $fp, -28($sp)
	sw $sp, -32($sp)
	sw $ra, -36($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 40
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	addi $t0, $zero, 0
	sw $t0, -8($fp)
	addi $t0, $zero, 1
	sw $t0, -12($fp)
	addi $t0, $zero, 0
	sw $t0, -20($fp)
	addi $t0, $zero, 0
	sw $t0, -24($fp)
	addi $t0, $zero, 10
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal allocate
	addi $sp, $sp, 4
	addi $t0, $v0, 0
	sw $t0, 0($fp)
	addi $t0, $zero, 0
	sw $t0, -4($fp)
	j test1
start1:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal allocate
	addi $sp, $sp, 4
	addi $t0, $v0, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sw $t0, 0($t2)
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
test1:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	bgtz $t0, start1
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal ClearBoard1
	j test2
start3:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal PrintBoard1
start2:
	addi $sp, $sp, 0
	jal Read
	addi $sp, $sp, 0
	addi $t0, $v0, 0
	sw $t0, -16($fp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	lw $t0, 16($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 16
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -4($sp)
	addi $sp, $sp, -8
	jal legal1
	addi $sp, $sp, 8
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t2, $zero, 1
	sub $t0, $t2, $t0
	bgtz $t0, start2
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	lw $t0, 16($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 16
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -4($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal DoMove1
	lw $t0, 20($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 20
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -20($fp)
	lw $t0, 16($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 16
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -4($sp)
	addi $sp, $sp, -8
	jal win1
	addi $sp, $sp, 8
	addi $t0, $v0, 0
	bgtz $t0, testPassed1
	j end2
testPassed1:
	addi $t0, $zero, 1
	sw $t0, -8($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -24($fp)
end2:
	lw $t0, 20($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 20
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 100
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch1
	addi $t0, $zero, 0
	j fallThrough1
branch1:
	addi $t0, $zero, 1
fallThrough1:
	bgtz $t0, testPassed2
	j end3
testPassed2:
	addi $t0, $zero, 1
	sw $t0, -8($fp)
	addi $t0, $zero, 0
	sw $t0, -24($fp)
end3:
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal swap1
	addi $sp, $sp, 4
	addi $t0, $v0, 0
	sw $t0, -12($fp)
test2:
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t2, $zero, 1
	sub $t0, $t2, $t0
	bgtz $t0, start3
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal PrintBoard1
	addi $sp, $sp, 0
	jal Println
	addi $sp, $sp, 0
	jal Println
	lw $t0, 24($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 24
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal Print
	addi $sp, $sp, 0
	jal Println
mainend1:
	lw $t0, 36($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 36
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 32($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 32
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 28($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 28
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
legal1:
	sw $fp, 0($sp)
	sw $sp, -4($sp)
	sw $ra, -8($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 9
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t0, $t2
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	or $t0, $t2, $t0
	bgtz $t0, testPassed3
	j end4
testPassed3:
	addi $t0, $zero, 0
	addi $v0, $t0, 0
	j legalend1
end4:
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch2
	addi $t0, $zero, 1
	j fallThrough2
branch2:
	addi $t0, $zero, 0
fallThrough2:
	bgtz $t0, testPassed4
	j end5
testPassed4:
	addi $t0, $zero, 0
	addi $v0, $t0, 0
	j legalend1
end5:
	addi $t0, $zero, 1
	addi $v0, $t0, 0
	j legalend1
legalend1:
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 0($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
ClearBoard1:
	sw $fp, -8($sp)
	sw $sp, -12($sp)
	sw $ra, -16($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 20
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	addi $t0, $zero, 0
	sw $t0, 0($fp)
	j test4
start5:
	addi $t0, $zero, 0
	sw $t0, -4($fp)
	j test3
start4:
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sw $t0, 0($t2)
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
test3:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	bgtz $t0, start4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($fp)
test4:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	bgtz $t0, start5
ClearBoardend1:
	lw $t0, 16($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 16
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 8($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
PrintBoard1:
	sw $fp, -8($sp)
	sw $sp, -12($sp)
	sw $ra, -16($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 20
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	addi $t0, $zero, 0
	sw $t0, 0($fp)
	j test6
start7:
	addi $t0, $zero, 0
	sw $t0, -4($fp)
	j test5
start6:
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal Print
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
test5:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	bgtz $t0, start6
	addi $sp, $sp, 0
	jal Println
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($fp)
test6:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	bgtz $t0, start7
	addi $sp, $sp, 0
	jal Println
	addi $t0, $zero, 0
	sw $t0, 0($fp)
	j test7
start8:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	addi $sp, $sp, -4
	jal Print
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($fp)
test7:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	bgtz $t0, start8
	addi $sp, $sp, 0
	jal Println
	addi $sp, $sp, 0
	jal Println
PrintBoardend1:
	lw $t0, 16($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 16
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 8($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
swap1:
	sw $fp, 0($sp)
	sw $sp, -4($sp)
	sw $ra, -8($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch3
	addi $t0, $zero, 0
	j fallThrough3
branch3:
	addi $t0, $zero, 1
fallThrough3:
	bgtz $t0, testPassed5
	addi $t0, $zero, 1
	addi $v0, $t0, 0
	j swapend1
	j end6
testPassed5:
	addi $t0, $zero, 2
	addi $v0, $t0, 0
	j swapend1
end6:
swapend1:
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 0($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
DoMove1:
	sw $fp, -4($sp)
	sw $sp, -8($sp)
	sw $ra, -12($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 16
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	addi $t0, $zero, 1
	sw $t0, 0($fp)
	j test8
start9:
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch4
	addi $t0, $zero, 1
	j fallThrough4
branch4:
	addi $t0, $zero, 0
fallThrough4:
	bgtz $t0, testPassed6
	j end7
testPassed6:
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sw $t0, 0($t2)
end7:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($fp)
test8:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	bgtz $t0, start9
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 36($t0)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 36
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sw $t0, 0($t2)
DoMoveend1:
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 4($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
win1:
	sw $fp, -16($sp)
	sw $sp, -20($sp)
	sw $ra, -24($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 28
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	addi $t0, $zero, 0
	sw $t0, 0($fp)
	j test9
start10:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($fp)
test9:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch5
	addi $t0, $zero, 0
	j fallThrough5
branch5:
	addi $t0, $zero, 1
fallThrough5:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start10
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch6
	addi $t0, $zero, 0
	j fallThrough6
branch6:
	addi $t0, $zero, 1
fallThrough6:
	bgtz $t0, testPassed7
	j end8
testPassed7:
	addi $t0, $zero, 0
	addi $v0, $t0, 0
	j winend1
end8:
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($fp)
	addi $t0, $zero, 0
	sw $t0, -4($fp)
	addi $t0, $zero, 0
	sw $t0, -12($fp)
	j test10
start11:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -12($fp)
test10:
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch7
	addi $t0, $zero, 0
	j fallThrough7
branch7:
	addi $t0, $zero, 1
fallThrough7:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start11
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	bgtz $t0, testPassed8
	j end9
testPassed8:
	addi $t0, $zero, 1
	addi $v0, $t0, 0
	j winend1
end9:
	addi $t0, $zero, 0
	sw $t0, -4($fp)
	addi $t0, $zero, 0
	sw $t0, -12($fp)
	j test11
start12:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -12($fp)
test11:
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch8
	addi $t0, $zero, 0
	j fallThrough8
branch8:
	addi $t0, $zero, 1
fallThrough8:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start12
	addi $t0, $zero, 1
	sw $t0, -12($fp)
	j test12
start13:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -12($fp)
test12:
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch9
	addi $t0, $zero, 0
	j fallThrough9
branch9:
	addi $t0, $zero, 1
fallThrough9:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start13
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	bgtz $t0, testPassed9
	j end10
testPassed9:
	addi $t0, $zero, 1
	addi $v0, $t0, 0
	j winend1
end10:
	addi $t0, $zero, 0
	sw $t0, -12($fp)
	addi $t0, $zero, 0
	sw $t0, -4($fp)
	j test13
start14:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -12($fp)
test13:
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch10
	addi $t0, $zero, 0
	j fallThrough10
branch10:
	addi $t0, $zero, 1
fallThrough10:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start14
	addi $t0, $zero, 1
	sw $t0, -12($fp)
	j test14
start15:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -12($fp)
test14:
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch11
	addi $t0, $zero, 0
	j fallThrough11
branch11:
	addi $t0, $zero, 1
fallThrough11:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start15
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	bgtz $t0, testPassed10
	j end11
testPassed10:
	addi $t0, $zero, 1
	addi $v0, $t0, 0
	j winend1
end11:
	addi $t0, $zero, 0
	sw $t0, -12($fp)
	addi $t0, $zero, 0
	sw $t0, -4($fp)
	j test15
start16:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -12($fp)
test15:
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch12
	addi $t0, $zero, 0
	j fallThrough12
branch12:
	addi $t0, $zero, 1
fallThrough12:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start16
	addi $t0, $zero, 1
	sw $t0, -12($fp)
	j test16
start17:
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -4($fp)
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 1
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, -12($fp)
test16:
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 10
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	add $t0, $t2, $t0
	sw $t0, 0($sp)
	lw $t0, 0($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	sw $t0, -4($sp)
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, -8($sp)
	addi $sp, $sp, -12
	jal GetBoard1
	addi $sp, $sp, 12
	addi $t0, $v0, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	beq $t2, $t0, branch13
	addi $t0, $zero, 0
	j fallThrough13
branch13:
	addi $t0, $zero, 1
fallThrough13:
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	and $t0, $t2, $t0
	bgtz $t0, start17
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	addi $t2, $t2, 1
	slt $t0, $t0, $t2
	bgtz $t0, testPassed11
	j end12
testPassed11:
	addi $t0, $zero, 1
	addi $v0, $t0, 0
	j winend1
end12:
	addi $t0, $zero, 0
	addi $v0, $t0, 0
	j winend1
winend1:
	lw $t0, 24($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 24
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 20($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 20
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 16($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 16
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
GetBoard1:
	sw $fp, 0($sp)
	sw $sp, -4($sp)
	sw $ra, -8($sp)
	addi $t0, $sp, 0
	addi $fp, $t0, 0
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	addi $sp, $t0, 0
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 9
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t0, $t2
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	or $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t2, $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	or $t0, $t2, $t0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 9
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	slt $t0, $t0, $t2
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	or $t0, $t2, $t0
	bgtz $t0, testPassed12
	j end13
testPassed12:
	addi $t0, $zero, 0
	addi $v0, $t0, 0
	j GetBoardend1
end13:
	lw $t0, -4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	lw $t0, -12($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, -12
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	mult $t0, $t2
	mflo $t0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $v0, $t0, 0
	j GetBoardend1
GetBoardend1:
	lw $t0, 8($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 8
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $ra, $t0, 0
	lw $t0, 4($fp)
	addi $t0, $fp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 4
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $sp, $t0, 0
	lw $t0, 0($sp)
	addi $t0, $sp, 0
	sw $t0, 0($t1)
	addi $t1, $t1, -4
	addi $t0, $zero, 0
	lw $t2, 4($t1)
	addi $t1, $t1, 4
	sub $t0, $t2, $t0
	lw $t0, 0($t0)
	addi $fp, $t0, 0
	jr $ra
Print:
	lw $a0, 4($sp)
	li $v0, 1
	syscall
	li $v0,4
	la $a0, sp
	syscall
	jr $ra
Println:
	li $v0,4
	la $a0, cr
	syscall
	jr $ra
Read:
	li $v0,5
	syscall
	jr $ra
allocate:
	la $t2, HEAPPTR
	lw $v0,0($t2)
	lw $t3, 4($sp)
	sub $t3,$v0,$t3
	sw $t3,0($t2)
	jr $ra
	.data
cr:
	.asciiz "\n"
sp:
	.asciiz " "
HEAPPTR:
	.word 0
