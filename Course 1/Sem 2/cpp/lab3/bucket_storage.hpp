#pragma once

#include <stdexcept>

template< typename C >
concept Copyable = std::is_copy_constructible_v< C >;

template< typename T >
class BucketStorage
{
  public:
	class iterator;
	class const_iterator;
	using value_type = T;
	using size_type = std::size_t;
	using reference = T&;
	using const_reference = const T&;
	using pointer = T*;
	using const_pointer = const T*;
	using difference_type = std::ptrdiff_t;
	friend class iterator;

  private:
	struct BucketNode;
	struct Block;
	struct StackNode;

	size_type node_counter = 0;
	size_type cap = 64;
	size_type bucket_size = 0;
	size_type block_count = 0;

	BucketNode* front = nullptr;
	BucketNode* back = nullptr;
	Block* av_block = nullptr;
	StackNode* stack_head = nullptr;

	void add_to_block(BucketNode* node, Block* block) noexcept;
	void add_to_order(BucketNode* node) noexcept;
	void delete_from_block(BucketNode* node) noexcept;
	void delete_from_order(BucketNode* node) noexcept;
	void delete_position(StackNode* position) noexcept;
	void delete_block(Block* block) noexcept;
	void insert(BucketNode* node);

  public:
	explicit BucketStorage(size_type capacity = 64);
	BucketStorage(const BucketStorage< T >& other);
	BucketStorage(BucketStorage< T >&& other) noexcept;
	~BucketStorage();
	BucketStorage< T >& operator=(const BucketStorage< T >& other);
	BucketStorage< T >& operator=(BucketStorage< T >&& other) noexcept;

	template< Copyable C = T >
	iterator insert(const C& value);
	iterator insert(value_type&& value);
	iterator erase(iterator it);
	bool empty() const noexcept;
	size_type size() const noexcept;
	size_type capacity() const noexcept;
	iterator begin() noexcept;
	const_iterator begin() const noexcept;
	const_iterator cbegin() const noexcept;
	iterator end() noexcept;
	const_iterator end() const noexcept;
	const_iterator cend() const noexcept;
	void shrink_to_fit() noexcept;
	void clear();
	void swap(BucketStorage& other) noexcept;
	iterator get_to_distance(iterator it, const difference_type distance) const noexcept;
};

template< typename T >
struct BucketStorage< T >::BucketNode
{
	value_type value;
	const size_type number;
	BucketNode* next_it = nullptr;
	BucketNode* prev_it = nullptr;
	BucketNode* next_bl = nullptr;
	BucketNode* prev_bl = nullptr;
	Block* block = nullptr;
	BucketNode(const value_type& value, size_type number) : value(value), number(number) {}
	BucketNode(value_type&& value, size_type number) : value(std::move(value)), number(number) {}
};

template< typename T >
struct BucketStorage< T >::Block
{
	size_type cur_size;
	BucketNode* last_node;
	Block* next = nullptr;
	Block* prev;
	StackNode* position = nullptr;
	Block(BucketNode* node, Block* prev) : cur_size(1), last_node(node), prev(prev) {}
};

template< typename T >
struct BucketStorage< T >::StackNode
{
	Block* block;
	StackNode* prev;
	StackNode* next = nullptr;
	explicit StackNode(Block* block, StackNode* prev) : block(block), prev(prev) {}
};

template< typename T >
void BucketStorage< T >::add_to_block(BucketStorage::BucketNode* node, BucketStorage::Block* block) noexcept
{
	node->prev_bl = block->last_node;
	block->last_node->next_bl = node;
	block->last_node = node;
	node->block = block;
	block->cur_size++;
}

template< typename T >
void BucketStorage< T >::add_to_order(BucketStorage::BucketNode* node) noexcept
{
	if (front == nullptr)
		front = node;
	node->prev_it = back;
	if (back != nullptr)
		back->next_it = node;
	back = node;
}

template< typename T >
void BucketStorage< T >::delete_from_block(BucketStorage::BucketNode* node) noexcept
{
	Block* block = node->block;
	BucketNode* prev = node->prev_bl;
	BucketNode* next = node->next_bl;
	if (prev != nullptr)
		prev->next_bl = next;
	if (next != nullptr)
		next->prev_bl = prev;
	else
		block->last_node = prev;
	block->cur_size--;
}

template< typename T >
void BucketStorage< T >::delete_from_order(BucketStorage::BucketNode* node) noexcept
{
	BucketNode* prev = node->prev_it;
	BucketNode* next = node->next_it;
	if (prev != nullptr)
		prev->next_it = next;
	else
		front = next;
	if (next != nullptr)
		next->prev_it = prev;
	else
		back = prev;
}

template< typename T >
void BucketStorage< T >::delete_position(BucketStorage::StackNode* position) noexcept
{
	position->block->position = nullptr;
	StackNode* prev = position->prev;
	StackNode* next = position->next;
	if (prev != nullptr)
		prev->next = next;
	if (next != nullptr)
		next->prev = prev;
	else
		stack_head = prev;
	delete position;
}

template< typename T >
void BucketStorage< T >::delete_block(BucketStorage::Block* block) noexcept
{
	Block* prev_block = block->prev;
	Block* next_block = block->next;
	if (prev_block != nullptr)
		prev_block->next = next_block;
	if (next_block != nullptr)
		next_block->prev = prev_block;
	if (block == av_block)
		av_block = block->prev;
	delete block;
	block_count--;
}

template< typename T >
void BucketStorage< T >::insert(BucketStorage::BucketNode* node)
{
	add_to_order(node);
	if (stack_head != nullptr)
	{
		add_to_block(node, stack_head->block);
		if (stack_head->block->cur_size == cap)
			delete_position(stack_head);
	}
	else if (av_block != nullptr && av_block->cur_size < cap)
		add_to_block(node, av_block);
	else
	{
		auto* new_block = new Block(node, av_block);
		node->block = new_block;
		if (av_block != nullptr)
			av_block->next = new_block;
		av_block = new_block;
		block_count++;
	}
	bucket_size++;
}

template< typename T >
class BucketStorage< T >::iterator : public std::iterator< std::bidirectional_iterator_tag, T >
{
	friend class BucketStorage< T >;

  protected:
	BucketNode* node;
	BucketNode* back;

  public:
	iterator(BucketNode* node, BucketNode* back) noexcept : node(node), back(back) {}

	iterator(const iterator& it) noexcept : node(it.node), back(it.back) {}

	iterator(iterator&& it) noexcept : node(it.node), back(it.back)
	{
		it.node = nullptr;
		it.back = nullptr;
	}

	iterator& operator=(const iterator& it) noexcept
	{
		if (this != &it)
		{
			node = it.node;
			back = it.back;
		}
		return *this;
	}

	iterator& operator=(iterator&& it) noexcept
	{
		if (this != &it)
		{
			node = it.node;
			back = it.back;
			it.node = nullptr;
			it.back = nullptr;
		}
		return *this;
	}

	bool operator==(const iterator& it) const noexcept { return node == it.node; }

	bool operator!=(const iterator& it) const noexcept { return node != it.node; }

	bool operator<(const iterator& it) const noexcept
	{
		return node != nullptr && (it.node != nullptr && node->number < it.node->number || it.node == nullptr);
	}

	bool operator>(const iterator& it) const noexcept
	{
		return it.node != nullptr && (node != nullptr && node->number > it.node->number || node == nullptr);
	}

	bool operator<=(iterator& it) const noexcept { return !this->operator>(it); }

	bool operator>=(iterator& it) const noexcept { return !this->operator<(it); }

	reference operator*() const
	{
		if (node != nullptr)
			return node->value;
		throw std::out_of_range("call operator * from end()");
	}

	pointer operator->() const
	{
		if (node != nullptr)
			return &(node->value);
		throw std::out_of_range("call operator -> from end()");
	}

	iterator& operator++() noexcept
	{
		if (node != nullptr)
			node = node->next_it;
		return *this;
	}

	iterator& operator--() noexcept
	{
		if (node == nullptr)
			node = back;
		else if (node->prev_it != nullptr)
			node = node->prev_it;
		return *this;
	}

	iterator operator++(int) noexcept
	{
		iterator temp = *this;
		++(*this);
		return temp;
	}

	iterator operator--(int) noexcept
	{
		iterator temp = *this;
		--(*this);
		return temp;
	}
};

template< typename T >
class BucketStorage< T >::const_iterator : public BucketStorage< T >::iterator
{
  public:
	const_iterator(BucketNode* node, BucketNode* back) noexcept : iterator(node, back) {}

	const_iterator(const const_iterator& it) noexcept : iterator(it) {}

	const_iterator(const_iterator&& it) noexcept : iterator(std::move(it)) {}

	const_reference operator*() const { return iterator::operator*(); }

	const_pointer operator->() const { return &(iterator::operator->); }
};

template< typename T >
BucketStorage< T >::BucketStorage(BucketStorage< T >::size_type capacity)
{
	if (capacity <= 0)
		throw std::invalid_argument("invalid capacity");
	cap = capacity;
}

template< typename T >
BucketStorage< T >::BucketStorage(const BucketStorage< T >& other)	  // the responsibility for checking the concept
																	  // will be shifted to the method insert
{
	static_assert(std::is_copy_constructible< value_type >::value, "copy constructor is not supported");
	cap = other.cap;
	for (const_iterator it = other.begin(); it < other.end(); it++)
		insert(*it);
}

template< typename T >
BucketStorage< T >::BucketStorage(BucketStorage< T >&& other) noexcept
{
	swap(other);
}

template< typename T >
BucketStorage< T >::~BucketStorage()
{
	clear();
}

template< typename T >
BucketStorage< T >& BucketStorage< T >::operator=(const BucketStorage< T >& other)	  // the responsibility for checking
																					  // the concept will be shifted to
																					  // the method insert
{
	static_assert(std::is_copy_constructible< value_type >::value, "copy assignment is not supported");
	if (this != &other)
	{
		clear();
		cap = other.cap;
		for (const_iterator it = other.begin(); it < other.end(); it++)
			insert(*it);
	}
	return *this;
}

template< typename T >
BucketStorage< T >& BucketStorage< T >::operator=(BucketStorage< T >&& other) noexcept
{
	if (this != &other)
	{
		clear();
		swap(other);
	}
	return *this;
}

template< typename T >
template< Copyable C >
typename BucketStorage< T >::iterator BucketStorage< T >::insert(const C& value)
{
	static_assert(std::is_copy_constructible< value_type >::value, "copy assignment is not supported");
	auto* node = new BucketNode(value, node_counter++);
	insert(node);
	return iterator(node, back);
}

template< typename T >
typename BucketStorage< T >::iterator BucketStorage< T >::insert(BucketStorage< T >::value_type&& value)
{
	auto* node = new BucketNode(std::move(value), node_counter++);
	insert(node);
	return iterator(node, back);
}

template< typename T >
typename BucketStorage< T >::iterator BucketStorage< T >::erase(BucketStorage< T >::iterator it)
{
	BucketNode* node = it.node;
	Block* block = node->block;
	BucketNode* next = node->next_it;
	delete_from_block(node);
	delete_from_order(node);
	delete node;
	bucket_size--;
	if (block->position != nullptr)
		delete_position(block->position);
	if (block->cur_size == 0)
		delete_block(block);
	else
	{
		stack_head = new StackNode(block, stack_head);
		block->position = stack_head;
	}
	return iterator(next, back);
}

template< typename T >
bool BucketStorage< T >::empty() const noexcept
{
	return bucket_size == 0;
}

template< typename T >
typename BucketStorage< T >::size_type BucketStorage< T >::size() const noexcept
{
	return bucket_size;
}

template< typename T >
typename BucketStorage< T >::size_type BucketStorage< T >::capacity() const noexcept
{
	return block_count * cap;
}

template< typename T >
typename BucketStorage< T >::iterator BucketStorage< T >::begin() noexcept
{
	return iterator(front, back);
}

template< typename T >
typename BucketStorage< T >::const_iterator BucketStorage< T >::begin() const noexcept
{
	return const_iterator(front, back);
}

template< typename T >
typename BucketStorage< T >::const_iterator BucketStorage< T >::cbegin() const noexcept
{
	return const_iterator(front, back);
}

template< typename T >
typename BucketStorage< T >::iterator BucketStorage< T >::end() noexcept
{
	return iterator(nullptr, back);
}

template< typename T >
typename BucketStorage< T >::const_iterator BucketStorage< T >::end() const noexcept
{
	return const_iterator(nullptr, back);
}

template< typename T >
typename BucketStorage< T >::const_iterator BucketStorage< T >::cend() const noexcept
{
	return const_iterator(nullptr, back);
}

template< typename T >
void BucketStorage< T >::shrink_to_fit() noexcept
{
	if (stack_head == nullptr)
		return;
	Block* cur_block = av_block;
	Block* free_block = stack_head->block;
	while (block_count * cap - bucket_size >= cap)
	{
		if (cur_block->cur_size == 0)
		{
			delete_block(cur_block);
			cur_block = av_block;
			continue;
		}
		if (cur_block == free_block || free_block->cur_size == cap)
		{
			delete_position(stack_head);
			if (stack_head == nullptr)
				break;
			free_block = stack_head->block;
			continue;
		}
		BucketNode* node = cur_block->last_node;
		delete_from_block(node);
		add_to_block(node, free_block);
	}
}

template< typename T >
void BucketStorage< T >::clear()
{
	iterator it = begin();
	while (it != end())
		it = erase(it);
}

template< typename T >
void BucketStorage< T >::swap(BucketStorage< T >& other) noexcept
{
	std::swap(node_counter, other.node_counter);
	std::swap(cap, other.cap);
	std::swap(bucket_size, other.bucket_size);
	std::swap(block_count, other.block_count);
	std::swap(front, other.front);
	std::swap(back, other.back);
	std::swap(av_block, other.av_block);
	std::swap(stack_head, other.stack_head);
}

template< typename T >
typename BucketStorage< T >::iterator
	BucketStorage< T >::get_to_distance(BucketStorage< T >::iterator it, const BucketStorage< T >::difference_type distance) const noexcept
{
	iterator new_it(it.node, back);
	if (distance < 0)
		for (difference_type i = 0; i > distance; i--)
			new_it--;
	else
		for (difference_type i = 0; i < distance; i++)
			new_it++;
	return new_it;
}
