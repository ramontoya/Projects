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
	addi $sp, $sp, 0
	jal Read
	addi $sp, $sp, 0
	addi $t0, $v0, 0
	sw $t0, 0($fp)
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
mainend1:
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
